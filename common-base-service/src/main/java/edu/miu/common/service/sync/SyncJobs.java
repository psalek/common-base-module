package edu.miu.common.service.sync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@ConditionalOnProperty(value = "common.sync.bootstrap", havingValue = "true")
public class SyncJobs {
	
	private boolean outage = false;

	private final Map<ScheduleType, List<Runnable>> runnablesMap = new HashMap<>();
	
	private void runSchedule(ScheduleType scheduleType) {
		if(outage) {
			log.debug("All scheduled tasks are stopped due to a planned outage");
			return;
		}
		
		log.debug("Starting to run the {} sync", scheduleType);
		
		List<Runnable> services = runnablesMap.get(scheduleType);
		if(null == services || services.isEmpty()) {
			return;
		}
		else {
			services.parallelStream().forEach(Runnable::run);
		}
	}
	
	// Everyday, from 10:30pm - 02:30am we have an outage period when no scheduled tasks are running
	@Scheduled(cron = "#{commonProperties.sync.schedule.outageStart}")
	public void outageStart() {
		outage = true;
	}

	// Everyday, from 10:30pm - 02:30am we have an outage period when no scheduled tasks are running
	@Scheduled(cron = "#{commonProperties.sync.schedule.outageEnd}")
	public void outageStop() {
		outage = false;
	}

	// Every month on the 27th of the month at 4:30am
	@Scheduled(cron = "#{commonProperties.sync.schedule.monthly}")
	public void monthlySync() {
		runSchedule(ScheduleType.MONTHLY);
	}

	// Every Monday at 3:30am
	@Scheduled(cron = "#{commonProperties.sync.schedule.weekly}")
	public void weeklySync() {
		runSchedule(ScheduleType.WEEKLY);
	}

	// Every day at 2:30am
	@Scheduled(cron = "#{commonProperties.sync.schedule.daily}")
	public void dailySync() {
		runSchedule(ScheduleType.DAILY);
	}

	// Twice a day starting at 6:30am
	@Scheduled(cron = "#{commonProperties.sync.schedule.twiceDaily}")
	public void twiceDailySync() {
		runSchedule(ScheduleType.TWICE_DAILY);
	}

	// Every 4 hours starting at 5:30am
	@Scheduled(cron = "#{commonProperties.sync.schedule.hexDaily}")
	public void hexDailySync() {
		runSchedule(ScheduleType.HEX_DAILY);
	}

	// Every hour of every day at minute 45 of the hour
	@Scheduled(cron = "#{commonProperties.sync.schedule.hourly}")
	public void hourlySync() {
		runSchedule(ScheduleType.HOURLY);
	}
	
	// Every 15 minutes at minutes 09, 24, 39, 54 of the hour
	@Scheduled(cron = "#{commonProperties.sync.schedule.fifteenMinutely}")
	public void fifteenMinutelySync() {
		runSchedule(ScheduleType.FIFTEEN_MINUTELY);
	}

	// Every minute at second 30 of the minute
	@Scheduled(cron = "#{commonProperties.sync.schedule.minutely}")
	public void minutelySync() {
		runSchedule(ScheduleType.MINUTELY);
	}

	// Every hour at office hours from 9 am to 9 pm
	@Scheduled(cron = "#{commonProperties.sync.schedule.hourlyOfficeHours}")
	public void hourlyOfficeHoursSync() {
		runSchedule(ScheduleType.HOURLY_OFFICE_HOURS);
	}

	public synchronized boolean addSyncService(Runnable service, ScheduleType scheduleType) {
		return addRunnable(service, scheduleType);
	}

	public synchronized boolean addRunnable(Runnable service, ScheduleType scheduleType) {
		List<Runnable> serviceList = runnablesMap.get(scheduleType);
		if(null == serviceList) {
			serviceList = new ArrayList<>();
			runnablesMap.put(scheduleType, serviceList);
		}
		else {
			if(serviceList.contains(service)) {
				return false;
			}
		}
		
		return serviceList.add(service);
	}

}
