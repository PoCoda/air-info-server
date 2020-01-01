package com.tondi.airinfoserver.model.status.PM;

import java.io.Serializable;

public class ParticlePollutionModel implements Serializable {
	private Double value;
	private Double percentage;

//	public ParticlePollutionModel(Double value, Double percentage) {
//		this.value = value;
//		this.percentage = percentage;
//	}
	
	public Double getValue() {
		return this.value;
	}
	
	public Double getPercentage() {
		return this.percentage;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
}
