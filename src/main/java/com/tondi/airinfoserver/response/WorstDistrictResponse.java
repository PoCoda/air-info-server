package com.tondi.airinfoserver.response;

import java.io.Serializable;

public class WorstDistrictResponse implements Serializable {
	private Double percentage;
	private String name;

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
