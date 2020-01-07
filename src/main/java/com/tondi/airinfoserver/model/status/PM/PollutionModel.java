package com.tondi.airinfoserver.model.status.PM;

import java.io.Serializable;

import com.tondi.airinfoserver.PollutionNorm;
import com.tondi.airinfoserver.PollutionType;

public class PollutionModel implements Serializable {
	private PollutionType type;
	private Double value;
	private Double percentage;

	public PollutionModel(PollutionType type) {
		this.type = type;
	}
	
	public PollutionType getType() {
		return type;
	}

	public void setType(PollutionType type) {
		this.type = type;
	}
	
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
	
	public static Double calculatePercentage(PollutionModel pm) {
		Double norm = PollutionNorm.getNormValueOfType(pm.getType());
		return pm.getValue() * 100 / norm;
	}
}
