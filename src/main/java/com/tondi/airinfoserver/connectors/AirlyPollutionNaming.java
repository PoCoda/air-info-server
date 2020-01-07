package com.tondi.airinfoserver.connectors;

import com.tondi.airinfoserver.PollutionType;

public enum AirlyPollutionNaming {
	PM10(PollutionType.PM10), 
	PM25(PollutionType.PM25);

	private PollutionType type;

	AirlyPollutionNaming(PollutionType type) {
		this.type = type;
	}
	
	public PollutionType getType() {
        return type;
    }

    public static String getAirlyKeyForType(PollutionType type){
        for(AirlyPollutionNaming e : AirlyPollutionNaming.values()){
            if(type == e.getType()) return e.name();
        }
        return null;
    }
}
