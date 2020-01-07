package com.tondi.airinfoserver;

public enum PollutionNorm {
	PM10(PollutionType.PM10, 50.0),
	PM25(PollutionType.PM25, 25.0);

	private Double value;
	private PollutionType type;

	PollutionNorm(PollutionType type, Double value) {
		this.type = type;
		this.value = value;
	}
	
	public PollutionType getType() {
		return type;
	}

	public void setType(PollutionType type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public static Double getNormValueOfType(PollutionType type){
        for(PollutionNorm e : PollutionNorm.values()){
            if(type.name() == e.name()) return e.getValue();
        }
        return null;
    }
}
