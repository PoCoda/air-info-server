package com.tondi.airinfoserver;

import java.util.Iterator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tondi.airinfoserver.model.status.CurrentStatusModel;
import com.tondi.airinfoserver.model.status.PM.ParticlePollutionModel;

//@Service
public class AirlyConnector {
	private String server = "https://airapi.airly.eu/v2";
	private RestTemplate rest;
	private HttpHeaders headers;
	private HttpStatus status;

	public AirlyConnector() {
		this.rest = new RestTemplate();
		this.headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Accept", "*/*");
		headers.add("apikey", System.getenv("AIRLY_API_KEY"));
	}

	public String get(String uri) {
		HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
		ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity,
				String.class);
		this.setStatus(responseEntity.getStatusCode());
		return responseEntity.getBody();
	}

	public CurrentStatusModel getCurrentPollutionForLatLng(Double lat, Double lng) {
		final String url = "/measurements/nearest?lat=" + lat.toString() + "&" + "lng=" + lng.toString();
		final String responseBody = this.get(url);
		return this.buildCurrentStatusModel(responseBody);
	}
	
	private CurrentStatusModel buildCurrentStatusModel(String response) {
		final ParticlePollutionModel pm10status = new ParticlePollutionModel();
		final ParticlePollutionModel pm25status = new ParticlePollutionModel();
		final JsonNode responseNode;

		ObjectMapper mapper = new ObjectMapper();
		try {
			responseNode = mapper.readTree(response);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new CurrentStatusModel();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new CurrentStatusModel();
		}
		
		Iterator<JsonNode> valuesIterator = responseNode.get("current").get("values").elements();
		while(valuesIterator.hasNext()) {
			JsonNode currentValue = valuesIterator.next();
			if(currentValue.get("name").asText().equals("PM10")) {
				pm10status.setValue(currentValue.get("value").asDouble());
				continue;
			}
			if(currentValue.get("name").asText().equals("PM25")) {
				pm25status.setValue(currentValue.get("value").asDouble());
				continue;
			}
		}
		
		Iterator<JsonNode> standardsIterator = responseNode.get("current").get("standards").elements();
		
		while(standardsIterator.hasNext()) {
			JsonNode currentValue = standardsIterator.next();
			if(currentValue.get("pollutant").asText().equals("PM10")) {
				pm10status.setPercentage(currentValue.get("percent").asDouble());
				continue;
			}
			if(currentValue.get("pollutant").asText().equals("PM25")) {
				pm25status.setPercentage(currentValue.get("percent").asDouble());
				continue;
			}
		}
		
		CurrentStatusModel status = new CurrentStatusModel();
		status.setPM10(pm10status);
		status.setPM25(pm25status);

		if(pm10status.getPercentage() > 100 || pm25status.getPercentage() > 100) {
			status.setMatchesNorms(false);
		}

		return status;	
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}