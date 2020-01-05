package com.tondi.airinfoserver.model.status;

import com.tondi.airinfoserver.model.status.PM.ParticlePollutionModel;

public class StatusModel implements Cloneable {

	private ParticlePollutionModel pm10;
	private ParticlePollutionModel pm25;
	boolean matchesNorms = true;

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setMatchesNorms(boolean value) {
		this.matchesNorms = value;
	}

	public void setPm10(ParticlePollutionModel pm10) {
		this.pm10 = pm10;
	}

	public void setPm25(ParticlePollutionModel pm25) {
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
