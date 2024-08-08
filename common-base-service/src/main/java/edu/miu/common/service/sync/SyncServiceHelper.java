package edu.miu.common.service.sync;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import edu.miu.common.domain.SyncEvent;
import edu.miu.common.domain.SyncLog;
import edu.miu.common.repository.SyncEventRepository;
import edu.miu.common.repository.SyncLogRepository;
import lombok.extern.slf4j.Slf4j;

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
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@ConditionalOnProperty(value = "common.sync.bootstrap", havingValue = "true")
public class SyncServiceHelper {
	
	
	@Autowired
	private SyncLogRepository syncLogRepository;
	
	@Autowired
	private SyncEventRepository syncEventRepository;
	
	public SyncLog createSyncLog(SyncLog syncLog) {
		return syncLogRepository.save(syncLog);
	}

	public SyncEvent createSyncEvent(String sourceServiceName, String targetServiceName) {
		SyncEvent syncEvent = syncEventRepository.save(new SyncEvent(sourceServiceName, targetServiceName));
		
		log.info("Following sync event started processing: {}", syncEvent);
			
		return syncEvent;
	}
	
	public SyncEvent closeSyncEvent(SyncEvent syncEvent) {
		log.info("Following sync event finished processing: {}", syncEvent);
		
		if(Objects.nonNull(syncEvent)) {
			syncEvent.finish();
			syncEvent = syncEventRepository.save(syncEvent);
		}
		
		return syncEvent;
	}
	
}
