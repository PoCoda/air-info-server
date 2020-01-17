package com.tondi.airinfoserver;

public enum District {
	Old_Town(50.066583, 19.940139, "Old Town"),
//	Grzegorzki(0.0, 0.0),
	Pradnik_Czerwony(50.090833, 19.971944, "Pradnik Czerwony"),
	Pradnik_Bialy(50.094473, 19.928860, "Pradnik Bialy"),
//	Krowodrza(0.0, 0.0),
	Bronowice(50.082394, 19.872105, "Bronowice"),
	Zwierzyniec(50.056602, 19.871141, "Zwierzyniec"),
	Debniki(50.050283, 19.927441, "Debniki"),
//	Lagiewniki_Borek_Falecki(0.0, 0.0),
//	Swoszowice(0.0, 0.0),
	Podgorze_Duchackie(50.013730, 19.965054, "Podgorze Duchackie"),
	Biezanow_Prokocim(50.016501, 20.028128, "Biezanow-Prokocim"),
	Podgorze(50.044972, 19.952269, "Podgorze"),
	Czyzyny(50.065962, 20.011438, "Czyzyny"),
	Mistrzejowice(50.097263, 20.005763, "Mistrzejowice"),
	Bienczyce(50.087071, 20.024263, "Bienczyce"),
	Wzgorza_Krzeslawickie(50.101408, 20.086917, "Wzgorza Krzeslawickie"),
	Nowa_Huta(50.068389, 20.091505, "Nowa Huta");
	
	private final Double lat;
    private final Double lng; 
    private final String name;
	
	District(Double lat, Double lng, String name) {
		this.lat = lat;
		this.lng = lng;
		this.name = name;
	}

	public Double getLat() {
		return lat;
	}

	public Double getLng() {
		return lng;
	}
	
	public String getName() {
		return name;
	}

}
