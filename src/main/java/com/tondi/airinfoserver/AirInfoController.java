package com.tondi.airinfoserver;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.tondi.airinfoserver.connectors.AirlyConnector;
import com.tondi.airinfoserver.db.DbConnector;
import com.tondi.airinfoserver.model.ResponseModel;
import com.tondi.airinfoserver.model.ResponseModelBuilder;
import com.tondi.airinfoserver.model.status.StatusModel;
import com.tondi.airinfoserver.response.StatusModelResponse;
import com.tondi.airinfoserver.response.StreakResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    
    @RequestMapping(value = "/streak", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String streak() {
    	
    	// TODO add conditional based on matching norms or extract to two endpoinds accepting status 
    	Boolean matchesNorms = false;
    	Integer daysOfStreak;
    	if(matchesNorms)
    		daysOfStreak = pollutionAnalyzer.getDaysOfMatchingNormsStreak();
    	else 
    		daysOfStreak = pollutionAnalyzer.getDaysOfExceedingNormsStreak();
    	
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