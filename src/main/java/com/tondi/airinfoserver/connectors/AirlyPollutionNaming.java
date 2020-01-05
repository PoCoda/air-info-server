package com.tondi.airinfoserver.connectors;

public enum AirlyPollutionNaming {
	PM10("PM10"), 
	PM25("PM25");

	private String valueName;

	AirlyPollutionNaming(String name) {
		this.valueName = name;
	}
	
	public String getName() {
        return valueName;
    }

    public static String getEnumKey(String code){
        for(AirlyPollutionNaming e : AirlyPollutionNaming.values()){
            if(code == e.name()) return e.getName();
        }
        return null;
    }
}
