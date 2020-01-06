package com.tondi.airinfoserver.daily_executor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.District;
import com.tondi.airinfoserver.connectors.AirlyConnector;
import com.tondi.airinfoserver.db.DbConnector;

@Component
public class DailyExecutor {
	ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

	@Autowired
	DailyAverageHandler averageHandler;
	volatile boolean isStopIssued;

	@Autowired
	AirlyConnector airlyConnector;

	private static final Logger logger = LoggerFactory.getLogger(DailyExecutor.class);

	public void startExecutionAt(int targetHour, int targetMin, int targetSec) {
		Runnable taskWrapper = new Runnable() {

			@Override
			public void run() {
				logger.info("Executing Daily Executor at: " + "" + LocalDateTime.now() + " " + ZoneId.systemDefault());
				averageHandler.execute();
				logger.info("Execution finished");
				startExecutionAt(targetHour, targetMin, targetSec);
			}

		};
		long delay = computeNextDelay(targetHour, targetMin, targetSec);
		ScheduledFuture<?> sf = executorService.schedule(taskWrapper, delay, TimeUnit.SECONDS);
		try {
			sf.get();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private long computeNextDelay(int targetHour, int targetMin, int targetSec) {
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.systemDefault();
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
		if (zonedNow.compareTo(zonedNextTarget) > 0)
			zonedNextTarget = zonedNextTarget.plusDays(1);

		Duration duration = Duration.between(zonedNow, zonedNextTarget);
//        return duration.getSeconds();
		return 1;
	}

	public void stop() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException ex) {
			logger.error(null, ex);
		}
	}
}