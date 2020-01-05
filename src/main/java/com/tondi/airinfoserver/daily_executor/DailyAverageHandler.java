package com.tondi.airinfoserver.daily_executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.District;
import com.tondi.airinfoserver.connectors.AirlyConnector;

@Service
public class DailyAverageHandler {
	@Autowired
	private AirlyConnector airlyConnector;

	public void execute() {
		this.fetchAveragePollution();
	}
	
	private void fetchAveragePollution() {
		System.out.println(airlyConnector);
		airlyConnector.getAverageHistoricalPollutionForLatLng(District.Old_Town.getLat(), District.Old_Town.getLng());
		System.out.println("After Execute");
	}
}
