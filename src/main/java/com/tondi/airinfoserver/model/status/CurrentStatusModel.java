package com.tondi.airinfoserver.model.status;

import com.tondi.airinfoserver.model.status.PM.ParticlePollutionModel;

public class CurrentStatusModel {

	private ParticlePollutionModel pm10model;
	private ParticlePollutionModel pm25model;
	boolean matches = true;

	public void setMatchesNorms(boolean value) {
		this.matches = value;
	}
	
	public void setPM10Model(ParticlePollutionModel model) {
		this.pm10model = model;
	}
	
	public void setPM25Model(ParticlePollutionModel model) {
		this.pm25model = model;
	}
	
	public boolean getMatchesNorms() {
		return matches;
	}

	public ParticlePollutionModel getPm10model() {
		return pm10model;
	}

	public ParticlePollutionModel getPm25model() {
		return pm25model;
	}
}
