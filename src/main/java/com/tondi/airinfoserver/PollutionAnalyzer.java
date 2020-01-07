package com.tondi.airinfoserver;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.db.DbConnector;
import com.tondi.airinfoserver.model.status.StatusModel;

@Service
public class PollutionAnalyzer {
	@Autowired
	DbConnector dbConnector;
	
	public Integer getDaysOfMatchingNormsStreak() {		
		LocalDate today = LocalDate.now();
		dbConnector.getAverageStatusFor(today);
		return 1;
	}
	
	// TODO check if it works well. Maybe some unit tests?
	public Integer getDaysOfExceedingNormsStreak() {
		LocalDate day = LocalDate.now();
		Integer daysStreak = 0;
//		System.out.println(dbConnector.getAverageStatusFor(today).getMatchesNorms());
		
		StatusModel queryResult = dbConnector.getAverageStatusFor(day);
		while(queryResult != null && !queryResult.getMatchesNorms()) {
			daysStreak++;
			queryResult = dbConnector.getAverageStatusFor(day.minusDays(daysStreak));
		}
		
		return daysStreak;
	}
}
