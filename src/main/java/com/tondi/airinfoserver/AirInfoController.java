package com.tondi.airinfoserver;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.tondi.airinfoserver.model.ResponseModel;
import com.tondi.airinfoserver.model.ResponseModelBuilder;
import com.tondi.airinfoserver.model.status.CurrentStatusModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tondi.airinfoserver.AirlyConnector;

@RestController
@EnableAutoConfiguration
//@CrossOrigin(origins = "http://localhost", maxAge = 3600)
//@ComponentScan(basePackages = "com.tondi")
public class AirInfoController {
	@Autowired
	private AirlyConnector airlyConnector;

//	private AirlyConnector airlyConnector = new AirlyConnector();
	HashMap<String, String> locations = new HashMap<String, String>();
	private ObjectMapper mapper = new ObjectMapper();
	
    @RequestMapping(value = "/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String current() {
    	CurrentStatusModel result = airlyConnector.getCurrentPollutionForLatLng(District.Old_Town.getLat(), District.Old_Town.getLng());

        try { 
            String jsonStr = mapper.writeValueAsString(result); 
            return jsonStr;
        } catch (IOException e) { 
            e.printStackTrace(); 
            return null;
        } 
    }
    
    @RequestMapping(value = "/streak", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    String streak() {
//    	CurrentStatusModel result = airlyConnector.getCurrentPollutionForLatLng(50.062006, 19.940984);	
//        try { 
//            String jsonStr = mapper.writeValueAsString(result); 
//            return jsonStr;
//        } catch (IOException e) { 
//            e.printStackTrace(); 
//            return null;
//        }
    	return null;
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

    public static void main(String[] args) {    	
        SpringApplication.run(AirInfoController.class, args);
    }

}