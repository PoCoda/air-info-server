package com.tondi.airinfoserver.db;

import java.util.List;
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
import org.springframework.stereotype.Service;

import com.tondi.airinfoserver.model.status.StatusModel;
import com.tondi.airinfoserver.model.status.PM.PollutionModel;

@Service
public class DbConnector {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger log = LoggerFactory.getLogger(DbConnector.class);
	
	public void createDailyMeasurementsTable() {
		log.info("Creating tables");
//		System.out.println(jdbcTemplate);
		jdbcTemplate.execute("DROP TABLE daily_measurements IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE daily_measurements (" + "id SERIAL, date DATE, pm10 FLOAT, pm25 FLOAT)");

	}

	public void addMeasurementsToDailyTable(StatusModel model) {

		createDailyMeasurementsTable();
		
		LocalDate localNow = LocalDate.now();
		System.out.println(localNow);

//		jdbcTemplate.update("INSERT INTO daily_measurements (pm10, pm25) VALUES (?, ?)", model.getPm10().getValue(), model.getPm25().getValue());
		jdbcTemplate.update("INSERT INTO daily_measurements (date, pm10, pm25) VALUES (?, ?, ?)", localNow, "40", "50");
		jdbcTemplate.update("INSERT INTO daily_measurements (date, pm10, pm25) VALUES (?, ?, ?)", localNow.plusDays(1), "30", "20");
		jdbcTemplate.update("INSERT INTO daily_measurements (date, pm10, pm25) VALUES (?, ?, ?)", localNow.plusDays(2), "50", "32");

		
//		jdbcTemplate.query("SELECT * FROM daily_measurements", (result) -> {
//			System.out.println(result);
//		});
//
//		// Split up the array of whole names into an array of first/last names
//		List<String[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
//				.map(name -> name.split(" "))
//				.collect(Collectors.toList());
//
//		// Use a Java 8 stream to print out each tuple of the list
//		splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));
//
//		// Uses JdbcTemplate's batchUpdate operation to bulk load data
//		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);
//
//		log.info("Querying for customer records where first_name = 'Josh':");
//		jdbcTemplate.query(
//				"SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
//				(rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
//		).forEach(customer -> log.info(customer.toString()));
	}

	public StatusModel getAverageStatusFor(LocalDate date) {
		if (date.isAfter(LocalDate.now())) {
			return new StatusModel(); // TODO throw?
		}
		
		String sql = "SELECT * FROM daily_measurements WHERE date = ?";

		return jdbcTemplate.queryForObject(sql, new Object[]{date.toString()}, (rs, rowNum) -> {
			StatusModel sm = new StatusModel();
			PollutionModel pm10 = new PollutionModel("PM10");
			pm10.setValue(rs.getDouble("pm10"));
			PollutionModel pm25 = new PollutionModel("PM25");
			pm25.setValue(rs.getDouble("pm25"));

			sm.setPm10(pm10);
			sm.setPm25(pm25);
			return sm;
		});
	}

}
