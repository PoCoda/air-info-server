package com.tondi.airinfoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tondi.airinfoserver.daily_executor.DailyExecutor;

@SpringBootApplication
public class AirInfoServerApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AirInfoServerApplication.class, args);
        context.getBean(DailyExecutor.class).startExecutionAt(0, 0, 0); // at each midnight
	}
}
