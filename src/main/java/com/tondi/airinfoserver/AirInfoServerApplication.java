package com.tondi.airinfoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AirInfoServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AirInfoServerApplication.class, args);
	}
}
