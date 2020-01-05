package com.tondi.airinfoserver.connectors;

import java.util.ArrayList;
import java.util.Iterator;

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
import com.tondi.airinfoserver.model.status.StatusModel;
import com.tondi.airinfoserver.model.status.PM.ParticlePollutionModel;

@Service
public class AirlyConnector implements PollutionServiceConnector {
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

	public StatusModel getCurrentPollutionForLatLng(Double lat, Double lng) {
		final String url = "/measurements/point?lat=" + lat.toString() + "&" + "lng=" + lng.toString();
		final String responseBody = this.get(url);
		return this.buildCurrentStatusModel(responseBody);
	}
	
	public StatusModel getAverageHistoricalPollutionForLatLng(Double lat, Double lng) {
		final String url = "/measurements/point?lat=" + lat.toString() + "&" + "lng=" + lng.toString();
		final String responseBody = this.get(url);
		return this.getDailyHistoricalAveragePollutionModel(responseBody);
	}

	private StatusModel buildCurrentStatusModel(String response) {
		ParticlePollutionModel pm10status = new ParticlePollutionModel("PM10");
		ParticlePollutionModel pm25status = new ParticlePollutionModel("PM25");
		final JsonNode responseNode;

		ObjectMapper mapper = new ObjectMapper();
		try {
			responseNode = mapper.readTree(response);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return new StatusModel();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new StatusModel();
		}

		Iterator<JsonNode> valuesIterator = responseNode.get("current").get("values").elements();
		
		while (valuesIterator.hasNext()) {
			JsonNode currentValue = valuesIterator.next();
			if (currentValue.get("name").asText().equals("PM10")) {
				pm10status.setValue(currentValue.get("value").asDouble());
				continue;
			}
			if (currentValue.get("name").asText().equals("PM25")) {
				pm25status.setValue(currentValue.get("value").asDouble());
				continue;
			}
		}
		
		// TODO do the same as with above
		Iterator<JsonNode> standardsIterator = responseNode.get("current").get("standards").elements();

		while (standardsIterator.hasNext()) {
			JsonNode currentValue = standardsIterator.next();
			if (currentValue.get("pollutant").asText().equals("PM10")) {
				pm10status.setPercentage(currentValue.get("percent").asDouble());
				continue;
			}
			if (currentValue.get("pollutant").asText().equals("PM25")) {
				pm25status.setPercentage(currentValue.get("percent").asDouble());
				continue;
			}
		}

		StatusModel status = new StatusModel();
		status.setPm10(pm10status);
		status.setPm25(pm25status);

		if (pm10status.getPercentage() > 100 || pm25status.getPercentage() > 100) {
			status.setMatchesNorms(false);
		}

		return status;
	}

//	private ParticlePollutionModel appendValuesToParticlePollutionModel(ParticlePollutionModel model, Iterator<JsonNode> valuesIterator) {
//		while (valuesIterator.hasNext()) {
//			JsonNode currentValue = valuesIterator.next();
//			if (currentValue.get("name").asText().equals(model.getName())) { // TODO getAirlyName(model.getname())
//				model.setValue(currentValue.get("value").asDouble());
//				continue;
//			}
//		}
//		return model;
//	}

	public StatusModel getDailyHistoricalAveragePollutionModel(String response) {
		final JsonNode responseNode;

		ObjectMapper mapper = new ObjectMapper();
		try {
			responseNode = mapper.readTree(response);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return null; // TODO
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null; 
		}
		
		ArrayList<StatusModel> hourlyModels = new ArrayList<StatusModel>();
 
		Iterator<JsonNode> dayIterator = responseNode.get("history").elements();
		while(dayIterator.hasNext()) {
			Iterator<JsonNode> valuesIterator = dayIterator.next().get("values").elements();
			
			ParticlePollutionModel pm10status = new ParticlePollutionModel("PM10");
			ParticlePollutionModel pm25status = new ParticlePollutionModel("PM25");
			
			// TODO try to use composite pattern as history contains same values as above and reuse code
			while (valuesIterator.hasNext()) {
				JsonNode currentValue = valuesIterator.next();
				if (currentValue.get("name").asText().equals("PM10")) {
					pm10status.setValue(currentValue.get("value").asDouble());
					continue;
				}
				if (currentValue.get("name").asText().equals("PM25")) {
					pm25status.setValue(currentValue.get("value").asDouble());
					continue;
				}
			}

			StatusModel hourlyStatus = new StatusModel();
			hourlyStatus.setPm10(pm10status);
			hourlyStatus.setPm25(pm25status);
			
			hourlyModels.add(hourlyStatus);
		}

		
		StatusModel average;
		try {
			average = (StatusModel) hourlyModels.get(0).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace(); 
			return new StatusModel();	 
		}
		
		for(StatusModel model : hourlyModels) {
			ParticlePollutionModel hourlyPm10 = model.getPm10();
			Double newPm10Value = (average.getPm10().getValue() + hourlyPm10.getValue()) / 2; 
			average.getPm10().setValue(newPm10Value);
			
			ParticlePollutionModel hourlyPm25 = model.getPm25();
			Double newPm25Value = (average.getPm25().getValue() + hourlyPm25.getValue()) / 2;
			average.getPm25().setValue(newPm25Value);
		}

		System.out.println(average.getPm10().getValue());
		return average;	
	}

	private String getAirlyPollutionIdentifierName(ParticlePollutionModel model) {
		String modelName = model.getName();
		return AirlyPollutionNaming.getEnumByString(modelName);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}