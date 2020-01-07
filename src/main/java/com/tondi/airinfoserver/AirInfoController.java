package com.tondi.airinfoserver;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tondi.airinfoserver.connectors.AirlyConnector;
import com.tondi.airinfoserver.model.status.StatusModel;
import com.tondi.airinfoserver.response.StatusModelResponse;
import com.tondi.airinfoserver.response.StreakResponse;

@RestController
@EnableAutoConfiguration
public class AirInfoController {
	@Autowired
	private AirlyConnector airlyConnector;
	@Autowired
	private PollutionAnalyzer pollutionAnalyzer; 
	private ObjectMapper mapper = new ObjectMapper();
	
    @RequestMapping(value = "/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String current() {
    	StatusModel currentStatus = airlyConnector.getCurrentPollutionForLatLng(District.Old_Town.getLat(), District.Old_Town.getLng());
    	StatusModelResponse response = new StatusModelResponse(currentStatus);
    	
        return this.serialize(response);
    }
    
    @RequestMapping(value = "/streak-matching", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String streakMatching() {
    	Integer daysOfStreak = pollutionAnalyzer.getDaysOfMatchingNormsStreak();
    	
    	StreakResponse response = new StreakResponse();
    	response.setDays(daysOfStreak);
    	
    	return this.serialize(response);  
	}
    
    @RequestMapping(value = "/streak-exceeding", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String streakExceeding() {
    	Integer daysOfStreak = pollutionAnalyzer.getDaysOfExceedingNormsStreak();
    	
    	StreakResponse response = new StreakResponse();
    	response.setDays(daysOfStreak);
    	
    	return this.serialize(response);  
	}
	
    
//    @RequestMapping(value = "/best-worst-since", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    String bestWorstSince() {
//    	return null;
//    }
//    
//    @RequestMapping(value = "/worst-district", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    String worstDistrict() {
//    	return null;
//    }
//
//    @RequestMapping(value = "/last-week-average", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    String lastWeekAverage() {
//    	return null;
//    }
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