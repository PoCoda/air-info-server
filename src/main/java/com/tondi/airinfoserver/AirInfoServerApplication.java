package com.tondi.airinfoserver;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tondi.airinfoserver.daily_executor.DailyAverageHandler;
import com.tondi.airinfoserver.daily_executor.DailyExecutor;

@SpringBootApplication
public class AirInfoServerApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AirInfoServerApplication.class, args);
        context.getBean(DailyExecutor.class).startExecutionAt(0, 0, 0); // at each midnight
	}
}
