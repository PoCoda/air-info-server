package com.tondi.airinfoserver;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tondi.airinfoserver.connectors.AirlyConnector;
import com.tondi.airinfoserver.model.status.StatusModel;
import com.tondi.airinfoserver.response.StatusModelResponse;
import com.tondi.airinfoserver.response.WorstDistrictResponse;
import com.tondi.airinfoserver.response.DaysResponse;
import com.tondi.airinfoserver.response.PercentageResponse;

@RestController
public class AirInfoController {
	@Autowired
	private AirlyConnector airlyConnector;
	private ObjectMapper mapper = new ObjectMapper();

	private HashMap<String, Serializable> lastResponses = new HashMap<>();

	private final String API_CURRENT = "/current";

	@RequestMapping(value = API_CURRENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String current() {
		return this.serialize(this.getCurrent());
	}

	private Serializable getCurrent() {
		System.out.println("updated !!!!! dupa");

		try {
			StatusModel currentStatus = airlyConnector.getCurrentPollutionForLatLng(District.Old_Town.getLat(),
					District.Old_Town.getLng());
			StatusModelResponse response = new StatusModelResponse(currentStatus);
			this.lastResponses.put(API_CURRENT, response);
			System.out.println("execute " + response);
			return response;
		} catch (HttpClientErrorException.TooManyRequests e) {
			return this.lastResponses.get(API_CURRENT);
		}
	}

	private String serialize(Object value) {
		try {
			String jsonStr = mapper.writeValueAsString(value);
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}