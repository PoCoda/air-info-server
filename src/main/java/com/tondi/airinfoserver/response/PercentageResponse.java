package com.tondi.airinfoserver.response;

import java.io.Serializable;

public class PercentageResponse implements Serializable {
	private Double percentage;

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
}
