package com.tondi.airinfoserver.connectors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import com.tondi.airinfoserver.model.status.PM.PollutionModel;

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
	
	public List<StatusModel> getHistoricalPollutionForLatLng(Double lat, Double lng) {
		final String url = "/measurements/point?lat=" + lat.toString() + "&" + "lng=" + lng.toString();
		final String responseBody = this.get(url);
		return this.getHourlyPollutionModels(responseBody);
	}

	private StatusModel buildCurrentStatusModel(String response) {
		PollutionModel pm10status = new PollutionModel("PM10");
		PollutionModel pm25status = new PollutionModel("PM25");
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

	public ArrayList<StatusModel> getHourlyPollutionModels(String response) {
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
 
		Iterator<JsonNode> dayIterator = responseNode.get("history").iterator();
		while(dayIterator.hasNext()) {
			Iterator<JsonNode> valuesIterator = dayIterator.next().get("values").iterator();
			
			PollutionModel pm10status = new PollutionModel("PM10");
			PollutionModel pm25status = new PollutionModel("PM25");
			
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
		
//		System.out.println(getAirlyPollutionIdentifierName(hourlyModels.get(0).getPm10()));

		return hourlyModels;
	}

	private String getAirlyPollutionIdentifierName(PollutionModel model) {
		String modelName = model.getName();
		return AirlyPollutionNaming.getEnumKey(modelName);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}