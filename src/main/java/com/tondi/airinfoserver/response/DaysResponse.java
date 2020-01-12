package com.tondi.airinfoserver.response;

import java.io.Serializable;

public class DaysResponse implements Serializable {
	private Integer days;

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}
}
