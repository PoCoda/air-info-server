package com.tondi.airinfoserver.model.status;

import com.tondi.airinfoserver.model.status.PM.ParticlePollutionModel;

public class CurrentStatusModel {

	private ParticlePollutionModel pm10;
	private ParticlePollutionModel pm25;
	boolean matchesNorms = true;

	public void setMatchesNorms(boolean value) {
		this.matchesNorms = value;
	}
	
	public void setPM10(ParticlePollutionModel pm10) {
		this.pm10 = pm10;
	}
	
	public void setPM25(ParticlePollutionModel pm25) {
		this.pm25 = pm25;
	}
	
	public boolean getMatchesNorms() {
		return matchesNorms;
	}

	public ParticlePollutionModel getPm10() {
		return pm10;
	}

	public ParticlePollutionModel getPm25() {
		return pm25;
	}
}
