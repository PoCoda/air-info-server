package com.tondi.airinfoserver.db;


import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class DbConnector {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger log = LoggerFactory.getLogger(DbConnector.class);

	private void addMeasurement() {
		// given measurement
		// insert to measurements
		
		
//		log.info("Creating tables");
//
		jdbcTemplate.execute("DROP TABLE day_measurements IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE day_measurements" +
				"id SERIAL, pm10 VARCHAR(255), pm25 VARCHAR(255))");
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
}

