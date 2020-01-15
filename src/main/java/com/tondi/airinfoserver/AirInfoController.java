package com.tondi.airinfoserver;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tondi.airinfoserver.connectors.AirlyConnector;
import com.tondi.airinfoserver.model.status.StatusModel;
import com.tondi.airinfoserver.response.StatusModelResponse;
import com.tondi.airinfoserver.response.DaysResponse;
import com.tondi.airinfoserver.response.PercentageResponse;

@RestController
@EnableAutoConfiguration
public class AirInfoController {
	@Autowired
	private AirlyConnector airlyConnector;
	@Autowired
	private PollutionAnalyzer pollutionAnalyzer;
	private ObjectMapper mapper = new ObjectMapper();

	private HashMap<String, Serializable> lastResponses = new HashMap<>();

	private final String API_CURRENT = "/current";
	private final String API_STREAK_MATCHING = "/streak-matching";
	private final String API_STREAK_EXCEEDING = "/streak-exceeding";
	private final String API_BEST_SINCE = "/best-since";
	private final String API_WORST_SINCE = "/worst-since";
	private final String API_THIS_WEEK_AVERAGE = "/this-week-average";
	private final String API_LAST_WEEK_AVERAGE = "/last-week-average";

	@RequestMapping(value = API_CURRENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String current() {
		return this.serialize(this.getCurrent());
	}

	@RequestMapping(value = API_STREAK_MATCHING, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String streakMatching() {
		return this.serialize(this.getStreakMatching());
	}

	@RequestMapping(value = API_STREAK_EXCEEDING, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String streakExceeding() {
		return this.serialize(this.getStreakExceeding());
	}

	@RequestMapping(value = API_BEST_SINCE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String bestSince() {
		return this.serialize(this.getBestSince());
	}

	@RequestMapping(value = API_WORST_SINCE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String worstSince() {
		return this.serialize(this.getWorstSince());
	}

//    @RequestMapping(value = "/worst-district", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    String worstDistrict() {
//    	return null;
//    }
//

	@RequestMapping(value = API_THIS_WEEK_AVERAGE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String thisWeekAverage() {
		return this.serialize(this.getThisWeekAverage());
	}

	@RequestMapping(value = API_LAST_WEEK_AVERAGE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	String lastWeekAverage() {
		return this.serialize(this.getLastWeekAverage());
	}

//    @RequestMapping(value = "/last-year", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    String lastYear() {
//    	return null;
//    }

	private Serializable getCurrent() {
		try {
			StatusModel currentStatus = airlyConnector.getCurrentPollutionForLatLng(District.Old_Town.getLat(),
					District.Old_Town.getLng());
			StatusModelResponse response = new StatusModelResponse(currentStatus);
			this.lastResponses.put(API_CURRENT, response);
			return response;
		} catch (HttpClientErrorException.TooManyRequests e) {
			return this.lastResponses.get(API_CURRENT);
		}
	}

	private Serializable getStreakMatching() {
		try {			
			Integer daysOfStreak = pollutionAnalyzer.getDaysMatchingNormsStreak();

			DaysResponse response = new DaysResponse();
			response.setDays(daysOfStreak);

			this.lastResponses.put(API_STREAK_MATCHING, response);
			return response;
		} catch (HttpClientErrorException.TooManyRequests e) {
			return this.lastResponses.get(API_STREAK_MATCHING);
		}
	}
	
	private Serializable getStreakExceeding() {
		try {			
			Integer daysOfStreak = pollutionAnalyzer.getDaysExceedingNormsStreak();

			DaysResponse response = new DaysResponse();
			response.setDays(daysOfStreak);

			this.lastResponses.put(API_STREAK_EXCEEDING, response);
			return response;
		} catch (HttpClientErrorException.TooManyRequests e) {
			return this.lastResponses.get(API_STREAK_EXCEEDING);
		}
	}
	
	private Serializable getBestSince() {
		try {
			Integer sinceDays = pollutionAnalyzer.getBestSinceDays();

			DaysResponse response = new DaysResponse();
			response.setDays(sinceDays);

			this.lastResponses.put(API_BEST_SINCE, response);
			return response;
		} catch (HttpClientErrorException.TooManyRequests e) {
			return this.lastResponses.get(API_BEST_SINCE);
		}
	}
	
	private Serializable getWorstSince() {
		try {
			Integer sinceDays = pollutionAnalyzer.getWorstSinceDays();

			DaysResponse response = new DaysResponse();
			response.setDays(sinceDays);

			this.lastResponses.put(API_WORST_SINCE, response);
			return response;
		} catch (HttpClientErrorException.TooManyRequests e) {
			return this.lastResponses.get(API_WORST_SINCE);
		}
	}
	
	private Serializable getThisWeekAverage() {
		try {
			Double average = pollutionAnalyzer.getThisWeekAverage();

			PercentageResponse response = new PercentageResponse();
			response.setPercentage(average);
			
			this.lastResponses.put(API_THIS_WEEK_AVERAGE, response);
			return response;
		} catch (HttpClientErrorException.TooManyRequests e) {
			return this.lastResponses.get(API_THIS_WEEK_AVERAGE);
		}
	}
	
	private Serializable getLastWeekAverage() {
		try {
			Double average = pollutionAnalyzer.getLastWeekAverage();

			PercentageResponse response = new PercentageResponse();
			response.setPercentage(average);
			
			this.lastResponses.put(API_LAST_WEEK_AVERAGE, response);
			return response;
		} catch (HttpClientErrorException.TooManyRequests e) {
			return this.lastResponses.get(API_LAST_WEEK_AVERAGE);
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