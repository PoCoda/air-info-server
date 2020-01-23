package com.tondi.airinfoserver.daily_executor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.District;
import com.tondi.airinfoserver.connectors.AirlyConnector;
import com.tondi.airinfoserver.db.DbConnector;
import com.tondi.airinfoserver.model.status.StatusModel;

@Service
public class DailyAverageHandler {
	@Autowired
	private AirlyConnector airlyConnector;
	@Autowired
	private DbConnector dbConnector;

	public void execute() {
		dbConnector.addStatusToDailyTable(airlyConnector.getHistoricalAveragePollution());
	}
}
