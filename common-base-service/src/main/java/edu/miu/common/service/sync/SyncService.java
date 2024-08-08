package edu.miu.common.service.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.common.domain.SyncEvent;
import edu.miu.common.domain.SyncFailure;
import edu.miu.common.domain.SyncLog;
import edu.miu.common.domain.SyncType;
import edu.miu.common.service.util.SyncErrorUtils;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <h1>Maharishi International University<br/>Computer Science Department</h1>
 * 
 * <p>Provides a common code for synchronizing a "target" from a "source". For each case, 
 * you will have to extend this class and also provide an implementation for the following:
 * {@link SyncSourceService}, {@link SyncTargetService}, and {@link SyncSourceToTargetConverter}</p>
 * 
 * <p>Pay attention that this is an abstract class and needs to be extended. A typical extension of this class 
 * would replace the generic parameters with actual types and also calls the {@link #synchronize()} method on a fixed schedule.</p>
 *
 * @author Payman Salek
 * 
 * @version 1.2.0
 * @since 1.2.0
 * 
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "common.sync.bootstrap", havingValue = "true")
public abstract class SyncService<Source, Target> implements Runnable {
	
	@Autowired
	private SyncJobs syncJobs;
	
	@Autowired
	private SyncServiceHelper syncServiceHelper;
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private SyncErrorUtils emailServiceClientUtils;
	
	@Value("${common.sync.master-switch-enabled:true}")
	private Boolean masterSwitchEnabled;
	
	private final boolean serviceEnabled;
	
	private final ScheduleType scheduleType;
	
	private SyncSourceService<Source> sourceService;
	
	private SyncTargetService<Target> targetService;
	
	private SyncSourceToTargetConverter<Source, Target> sourceToTargetConverter;
	
	protected SyncService(SyncSourceToTargetConverter<Source, Target> sourceToTargetConverter,
			SyncSourceService<Source> sourceService, 
			SyncTargetService<Target> targetService, ScheduleType scheduleType, boolean serviceEnabled) {
		super();
		this.sourceToTargetConverter = sourceToTargetConverter;
		this.sourceService = sourceService;
		this.targetService = targetService;
		this.scheduleType = scheduleType;
		this.serviceEnabled = serviceEnabled;
	}
	
	@PostConstruct
	private void init() {
		syncJobs.addSyncService(this, scheduleType);
	}

	/**
	 * <p>Performs a sync from a "source" system to a "target" system. 
	 * This method first reads a list from source and then synchronizes all the data elements from that list 
	 * to the target system. Here are the steps of the algorithm:</p>
	 * <ol>
	 * 	<li>Read a list of items from the "source" system</li>
	 * 	<li>Read a list of items from the "target" system</li>
	 * 	<li>Loop through "source" elements</li>
	 * 	<li>If "source" element "matches" an element from the "target" list, then:</li>
	 * 	<ul>
	 * 		<li>Remove target from target list</li>
	 * 		<li>If source "equates" target, do nothing</li>
	 * 		<li>If source and target don't equate, then "UPDATE" target with new values from "converted" source</li>
	 * 	</ul>
	 * 	<li>If no match is found in the target list for the source element, then "CREATE" a new element in the target system</li>
	 * 	<li>After going through all the elements of the source list, target list should be empty 
	 * 	(since we are removing elements from the target list as they match source elements). 
	 * 	But if there are any elements left, "REMOVE" them all from the target system.</li>
	 * </ol>
	 * 
	 * <p>Note the difference between "match" and "equate". "Match" normally compares the keys of the source 
	 * and target records, to see if they are the same records (implementation details are hidden in the "converter" 
	 * and vary case by case). "Equate" on the other hand, attempts to see if all fields in the "converted source" are 
	 * equivalent to the "target" element.</p>
	 * 
	 * <p>See: {@link SyncSourceService}, {@link SyncTargetService}, and {@link SyncSourceToTargetConverter}</p>
	 */
	protected List<SyncFailure<Source>> synchronize(final SyncEvent syncEvent) {
	
		List<SyncFailure<Source>> syncFailures = new ArrayList<>();
		
		log.info("Calling {} to retrieve a list", sourceService.getName());
		List<Source> sourceList = sourceService.retrieveList();
		
		log.info("Calling {} to retrieve a list", targetService.getName());
		List<Target> targetList = targetService.retrieveList();

		syncEvent.setSourceCount(sourceList.size());
		for(Source source : sourceList) {
			Target convertedSource = sourceToTargetConverter.convert(source);

			Optional<Target> optionalTarget = targetList.parallelStream()
				.filter(target -> sourceToTargetConverter.match(convertedSource, target))
				.findAny();
			
			if(optionalTarget.isPresent()) {
				Target matchedTarget = optionalTarget.get();
				targetList.remove(matchedTarget);
				if(sourceToTargetConverter.equate(matchedTarget, convertedSource)) {					
					log.debug("Elements are equal: {}", matchedTarget);
				}
				else {
					Target target = doSync(syncEvent, SyncType.UPDATE, source, sourceToTargetConverter.merge(source, matchedTarget));
					if(target == null) syncFailures.add(new SyncFailure<>(source, SyncType.UPDATE));
				}
			}
			else {
				Target target = doSync(syncEvent, SyncType.CREATE, source, convertedSource);
				if(target == null) syncFailures.add(new SyncFailure<>(source, SyncType.CREATE));
			}
		}
		
		targetList.forEach(target -> doSync(syncEvent, SyncType.DELETE, null, target));
		
		return syncFailures;
	}

	private Target doSync(SyncEvent event, SyncType syncType, Source source, Target target) {
		
		SyncLog logEntry = SyncLog.generateSyncLogEntry(event, syncType);
		
		try {
			switch(syncType) {
			case CREATE:
				target = targetService.create(target);
				if(Objects.nonNull(target)) {
					event.incrementCreateCount();
				}
				break;
			case UPDATE:
				target = targetService.update(target);
				if(Objects.nonNull(target)) {
					event.incrementUpdateCount();
				}
				break;
			case DELETE:
				target = targetService.delete(target);
				if(Objects.nonNull(target)) {
					event.incrementDeleteCount();
				}
				break;
			}
			
			if(Objects.nonNull(source)) {
				logEntry.setSourceValue(objectMapper.writeValueAsString(sourceToTargetConverter.convert(source)));
			}
			
			if(Objects.nonNull(target)) {
				logEntry.setTargetValue(objectMapper.writeValueAsString(target));
				syncServiceHelper.createSyncLog(logEntry);
			}
			
			return target;
		}
		catch(Throwable t) {
			log.error("A throwable happened while doSync {} ", target, t);
			
			return null;
		}
		
	}
	
	public void run() {
		if(!masterSwitchEnabled || !serviceEnabled) {
			// Abort if masterSwitch is NOT enabled - no sync at this time
			log.debug("Bypassing synchronization between {} and {} since master switch = {} and service switch = {}",
					sourceService.getName(), targetService.getName(), masterSwitchEnabled, serviceEnabled);

			return;
		}

		if(targetService.pauseSync()) {
			// temporarily pause sync based on logic inside pauseSync
			log.debug("Sync between {} and {} is temporarily paused", sourceService.getName(), targetService.getName());
			return;
		}

		SyncEvent syncEvent = null;
		try {
			// Create a new Sync Event
			syncEvent = syncServiceHelper.createSyncEvent(sourceService.getName(), targetService.getName());
			
			log.info("Following sync event started processing: {}", syncEvent);
			
			List<SyncFailure<Source>> response = synchronize(syncEvent);
			log.warn("A total of {} elements from source service were not synced as of {}", response.size(), LocalDateTime.now());
			response.forEach(i -> log.info("Student = {}", i));
		}
		catch(FeignException e) {
			log.error("A FeignClientException happened while syncing {} to {} with Message: {}", sourceService.getName(), targetService.getName(), e.getMessage());
			emailServiceClientUtils.generateSyncError(sourceService.getName(), targetService.getName(), e);
			throw e;
		}
		catch(Throwable t) {
			log.error("A throwable happened while syncing {} to {}", sourceService.getName(), targetService.getName(), t);
		}
		finally {
			syncServiceHelper.closeSyncEvent(syncEvent);
		}
	}

}
