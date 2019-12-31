package com.tondi.airinfoserver.model.status.PM;

import java.io.Serializable;

public class ParticlePollutionModel implements Serializable {
	private Double value;
	private Double percentage;
	
	public ParticlePollutionModel(Double value, Double percentage) {
		this.value = value;
		this.percentage = percentage;
	}
	
	public String getValue() {
		return this.value.toString();
	}
	
	public Double getPercentage() {
		return this.percentage;
	}
}
