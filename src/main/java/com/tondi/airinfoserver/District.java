package com.tondi.airinfoserver;

public enum District {
	Stare_Miasto(50.066583, 19.940139),
	Grzegorzki(0.0, 0.0),
	Pradnik_Czerwony(0.0, 0.0),
	Pradnik_Bialy(0.0, 0.0),
	Krowodrza(0.0, 0.0),
	Bronowice(0.0, 0.0),
	Zwierzyniec(0.0, 0.0),
	Debniki(0.0, 0.0),
	Lagiewniki_Borek_Falecki(0.0, 0.0),
	Swoszowice(0.0, 0.0),
	Podgorze_Duchackie(0.0, 0.0),
	Biezanow_Prokocim(0.0, 0.0),
	Podgorze(0.0, 0.0),
	Czyzyny(0.0, 0.0),
	Mistrzejowice(0.0, 0.0),
	Bienczyce(0.0, 0.0),
	Wzgorza_Krzeslawickie(0.0, 0.0),
	Nowa_Huta(0.0, 0.0);
	
	private final Double lat;
    private final Double lng; 
	
	District(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;
	}
}
