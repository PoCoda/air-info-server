package com.tondi.airinfoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.db.DbConnector;

@Service
public class PollutionAnalyzer {
	@Autowired
	DbConnector dbConnector;
	
	public int getDaysOfMatchingNormsStreak() {
		Boolean currentlyMatchesNorms = true;
		
		return 1;
	}
	
	public int getDaysOfExceedingNormsStreak() {
		return 4;
	}
}
