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
    	CurrentStatusModel result = airlyConnector.getCurrentPollutionForLatLng(District.Stare_Miasto.getLat(), District.Stare_Miasto.getLng());	
         

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
    	CurrentStatusModel result = airlyConnector.getCurrentPollutionForLatLng(50.062006, 19.940984);	
    	
        ObjectMapper mapper = new ObjectMapper(); 

        try { 
            String jsonStr = mapper.writeValueAsString(result); 
            return jsonStr;
        } catch (IOException e) { 
            e.printStackTrace(); 
            return null;
        } 
    }

    public static void main(String[] args) {    	
        SpringApplication.run(AirInfoController.class, args);
    }

}