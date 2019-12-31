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
		JsonNode responseNode;
		Double pm10value = 0.0; // TODO check if its good for flow control
		Double pm25value = 0.0;
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			responseNode = mapper.readTree(responseBody);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new CurrentStatusModel(); // TODO return EmptyCurrentStatusModel
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new CurrentStatusModel();
		}
		
		Iterator<JsonNode> i = responseNode.get("current").get("values").elements();
		
		while(i.hasNext()) {
			JsonNode currentValue = i.next();
			if(currentValue.get("name").asText().equals("PM10")) {
				pm10value = currentValue.get("value").asDouble();
				break;
			}
			if(currentValue.get("name").asText().equals("PM25")) {
				pm25value = currentValue.get("value").asDouble();
				break;
			}
		}
		
		ParticlePollutionModel pm10model = new ParticlePollutionModel(pm10value, 22.2);
		ParticlePollutionModel pm25model = new ParticlePollutionModel(pm25value, 22.2);
		CurrentStatusModel status = new CurrentStatusModel();
		status.setPM10Model(pm10model);
		status.setPM25Model(pm25model);
		
		
//	  mapper.readValue("{\"name\": \"John\"}", Person.class);

		return status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}