package com.tondi.airinfoserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tondi.airinfoserver.connectors.AirlyConnector;
import com.tondi.airinfoserver.model.status.StatusModel;
import com.tondi.airinfoserver.response.StatusModelResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

@RestController
public class AirInfoController {
	@Autowired
	private AirlyConnector airlyConnector;
	private ObjectMapper mapper = new ObjectMapper();

	private HashMap<String, Serializable> lastResponses = new HashMap<>();

	private final String API_CURRENT = "/current";
	private static final Logger logger = LoggerFactory.getLogger(AirInfoController.class);

	@RequestMapping(value = API_CURRENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String current() {
		return this.serialize(this.getCurrent());
	}

	private Serializable getCurrent() {
		try {
			StatusModel currentStatus = airlyConnector.getCurrentPollutionForLatLng(District.Old_Town.getLat(),
					District.Old_Town.getLng());
			StatusModelResponse response = new StatusModelResponse(currentStatus);
			this.lastResponses.put(API_CURRENT, response);

			logger.info("Executing air pollution fetch: " + response);
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