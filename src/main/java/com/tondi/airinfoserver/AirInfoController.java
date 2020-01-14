package com.tondi.airinfoserver;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
	@Cacheable("current")
    @RequestMapping(value = "/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String current() {
    	StatusModel currentStatus = airlyConnector.getCurrentPollutionForLatLng(District.Old_Town.getLat(), District.Old_Town.getLng());
    	StatusModelResponse response = new StatusModelResponse(currentStatus);
    	
        return this.serialize(response);
    }
	
	@Cacheable("streakMatching")
    @RequestMapping(value = "/streak-matching", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String streakMatching() {
    	Integer daysOfStreak = pollutionAnalyzer.getDaysMatchingNormsStreak();
    	
    	DaysResponse response = new DaysResponse();
    	response.setDays(daysOfStreak);
    	
    	return this.serialize(response);  
	}
    
	@Cacheable("streakExceeding")
    @RequestMapping(value = "/streak-exceeding", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String streakExceeding() {
    	Integer daysOfStreak = pollutionAnalyzer.getDaysExceedingNormsStreak();
    	
    	DaysResponse response = new DaysResponse();
    	response.setDays(daysOfStreak);
    	
    	return this.serialize(response);  
	}
	
	@Cacheable("bestSince")
    @RequestMapping(value = "/best-since", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String bestSince() {
    	Integer sinceDays = pollutionAnalyzer.getBestSinceDays();
    	
    	DaysResponse response = new DaysResponse();
    	response.setDays(sinceDays);
    	
    	return this.serialize(response);
	}
	
	@Cacheable("worstSince")
    @RequestMapping(value = "/worst-since", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String worstSince() {
    	Integer sinceDays = pollutionAnalyzer.getWorstSinceDays();
    	
    	DaysResponse response = new DaysResponse();
    	response.setDays(sinceDays);
    	
    	return this.serialize(response); 
	}
    
//    @RequestMapping(value = "/worst-district", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    String worstDistrict() {
//    	return null;
//    }
//
	
	@Cacheable("lastWeekAverage")
    @RequestMapping(value = "/last-week-average", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String lastWeekAverage() {
    	Double average = pollutionAnalyzer.getLastWeekAverage();
    	
    	PercentageResponse response = new PercentageResponse();
    	response.setPercentage(average);
    	
    	return this.serialize(response);
    }
    
	@Cacheable("thisWeekAverage")
    @RequestMapping(value = "/this-week-average", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String thisWeekAverage() {
    	Double average = pollutionAnalyzer.getThisWeekAverage();
    	
    	PercentageResponse response = new PercentageResponse();
    	response.setPercentage(average);
    	
    	return this.serialize(response);
    }
//    
//    @RequestMapping(value = "/last-year", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    String lastYear() {
//    	return null;
//    }
    
    private String serialize(Object value) {
    	try { 
            String jsonStr = mapper.writeValueAsString(value); 
            return jsonStr;
        } catch (IOException e) { 
            e.printStackTrace(); 
            return null;
        }     
    }
    
    // TODO necessary?
    public static void main(String[] args) {    	
        SpringApplication.run(AirInfoController.class, args);
    }

}