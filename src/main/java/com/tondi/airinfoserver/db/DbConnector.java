package com.tondi.airinfoserver.db;

import java.util.List;
import java.util.Random;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.PollutionType;
import com.tondi.airinfoserver.model.status.StatusModel;
import com.tondi.airinfoserver.model.status.PM.PollutionModel;

@Service
public class DbConnector {

//	JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource("jdbc:mysql://localhost:3306", "root", "", false));
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger log = LoggerFactory.getLogger(DbConnector.class);
	
//	public void addRandomValuesToDailyMeasurementsTable() {
//		log.info("Creating tables");
//		jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS db_air_info"); // create 
//		jdbcTemplate.execute("DROP TABLE IF EXISTS daily_measurements");
//		jdbcTemplate.execute("CREATE TABLE daily_measurements (" + "id SERIAL, date DATE, pm10 FLOAT, pm25 FLOAT)");
//
//		LocalDate localNow = LocalDate.now();
//		String today = localNow.toString();
//		String yesterday = localNow.minusDays(1).toString();
//		String twoDaysAgo = localNow.minusDays(2).toString();
//		String threeDaysAgo = localNow.minusDays(3).toString();
//
//		System.out.println(localNow);
//
//		for(int i = 0; i < 14; i++) {
//			jdbcTemplate.update("INSERT INTO daily_measurements (date, pm10, pm25) VALUES (?, ?, ?)", localNow.minusDays(i).toString(), Math.random() * 100, Math.random() * 50);
//		}
//	}

	public void addStatusToDailyTable(StatusModel model) {

//		addRandomValuesToDailyMeasurementsTable();
		
		LocalDate localNow = LocalDate.now();
		String today = localNow.toString();
		
		jdbcTemplate.update("INSERT INTO daily_measurements (date, pm10, pm25) VALUES (?, ?, ?)", today, model.getPm10().getValue(), model.getPm25().getValue());
		log.debug("Inserted into daily_measurements" + "PM10" + model.getPm10().getValue() + ", " + "PM25" + model.getPm25().getValue());
	}

	public StatusModel getAverageStatusForDay(LocalDate date) throws IllegalArgumentException {
		if (date.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Historical date " + date + "cannot be in future");
		}
		
		String sql = "SELECT * FROM daily_measurements WHERE date = ?";

		List<StatusModel> results = jdbcTemplate.query(sql, new Object[]{date.toString()}, (rs, rowNum) -> {
			StatusModel sm = new StatusModel();
			PollutionModel pm10 = new PollutionModel(PollutionType.PM10);
			pm10.setValue(rs.getDouble("pm10"));
			pm10.setPercentage(PollutionModel.calculatePercentage(pm10));
			PollutionModel pm25 = new PollutionModel(PollutionType.PM25);
			pm25.setValue(rs.getDouble("pm25"));
			pm25.setPercentage(PollutionModel.calculatePercentage(pm25));

			sm.setPm10(pm10);
			sm.setPm25(pm25);
			sm.setMatchesNorms(StatusModel.calculateMatchesNorms(sm));
			
			return sm;
		});
		
		if(results.size() == 1) {
			return results.get(0);
		}
		
		return null;
	}

}
