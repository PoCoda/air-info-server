package com.tondi.airinfoserver.connectors;

import com.tondi.airinfoserver.model.status.StatusModel;

public interface PollutionServiceConnector {
	public StatusModel getCurrentPollutionForLatLng(Double lat, Double lng);
}
