package com.tondi.airinfoserver.connectors;

public enum AirlyPollutionNaming {
	PM10("PM10"), 
	PM25("PM25");

	private String name;

	AirlyPollutionNaming(String name) {
		this.name = name;
	}
	
	public String getName() {
        return name;
    }
	
    public String toString(){ // TODO
        return name;
    }

    public static String getEnumByString(String code){
        for(AirlyPollutionNaming e : AirlyPollutionNaming.values()){
            if(code == e.name) return e.name();
        }
        return null;
    }
}
