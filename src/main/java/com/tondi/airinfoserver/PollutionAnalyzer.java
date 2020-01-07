package com.tondi.airinfoserver;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.db.DbConnector;

@Service
public class PollutionAnalyzer {
	@Autowired
	DbConnector dbConnector;
	
	public int getDaysOfMatchingNormsStreak() {		
		LocalDate today = LocalDate.now();
		dbConnector.getAverageStatusFor(today);
		return 1;
	}
	
	public int getDaysOfExceedingNormsStreak() {
		LocalDate today = LocalDate.now();
		dbConnector.getAverageStatusFor(today);
		return 4;
	}
}
