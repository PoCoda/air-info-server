package com.tondi.airinfoserver.daily_executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.connectors.AirlyConnector;
import com.tondi.airinfoserver.db.DbConnector;

@Service
@EnableScheduling
public class DailyAverageHandler {
	@Autowired
	private AirlyConnector airlyConnector;
	@Autowired
	private DbConnector dbConnector;

	@Scheduled(cron="0 55 23 1/1 * ?", zone="Poland")
	public void execute() {
		dbConnector.addStatusToDailyTable(airlyConnector.getHistoricalAveragePollution());
	}
}
