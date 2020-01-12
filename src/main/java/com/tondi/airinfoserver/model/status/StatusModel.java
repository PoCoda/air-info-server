package com.tondi.airinfoserver.model.status;

import java.io.Serializable;
import java.util.List;

import com.tondi.airinfoserver.model.status.PM.PollutionModel;

public class StatusModel implements Cloneable, Serializable {

	private PollutionModel pm10;
	private PollutionModel pm25;
	boolean matchesNorms = true;

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setMatchesNorms(boolean value) {
		this.matchesNorms = value;
	}

	public void setPm10(PollutionModel pm10) {
		this.pm10 = pm10;
	}

	public void setPm25(PollutionModel pm25) {
		this.pm25 = pm25;
	}

	public boolean getMatchesNorms() {
		return matchesNorms;
	}

	public PollutionModel getPm10() {
		return pm10;
	}

	public PollutionModel getPm25() {
		return pm25;
	}
	
	public Double calculateHarmFactor() {
		return (this.getPm10().getValue() + 2 * this.getPm25().getValue()) / 3;
	}
	
	public Double calculateHarmFactorPercentage() {
		return (this.getPm10().getPercentage()) + 2 * this.getPm25().getPercentage() / 3;
	}

	public static StatusModel getAveragedStatus(List<StatusModel> statusList) {

		StatusModel average;
		try {
			average = (StatusModel) statusList.get(0).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return new StatusModel();
		}

		for (StatusModel model : statusList) {
			PollutionModel hourlyPm10 = model.getPm10();
			Double newPm10Value = (average.getPm10().getValue() + hourlyPm10.getValue()) / 2;
			average.getPm10().setValue(newPm10Value);

			PollutionModel hourlyPm25 = model.getPm25();
			Double newPm25Value = (average.getPm25().getValue() + hourlyPm25.getValue()) / 2;
			average.getPm25().setValue(newPm25Value);
		}

		return average;

	}
	
	public static Boolean calculateMatchesNorms(StatusModel status) {
		if(status.getPm10().getPercentage() == null || status.getPm25().getPercentage() == null) {
			throw new IllegalArgumentException("Cannot calculate percentage - Passed object has some PollutionModel's percentage set to null");
		}
		
		if (status.getPm10().getPercentage() > 100 || status.getPm25().getPercentage() > 100) {
			return false;
		}
		return true;
	}
}
