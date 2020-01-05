package com.tondi.airinfoserver.model.status;

import java.util.List;

import com.tondi.airinfoserver.model.status.PM.PollutionModel;

public class StatusModel implements Cloneable {

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

//		System.out.println(average.getPm10().getValue());

		return average;

	}
}
