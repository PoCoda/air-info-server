package com.tondi.airinfoserver.model.status.PM;

import java.io.Serializable;

public class PollutionModel implements Serializable {
	private String name;
	private Double value;
	private Double percentage;

	public PollutionModel(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
