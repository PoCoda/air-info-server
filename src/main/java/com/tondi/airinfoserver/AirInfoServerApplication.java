package com.tondi.airinfoserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tondi.airinfoserver.daily_executor.DailyAverageHandler;
import com.tondi.airinfoserver.daily_executor.DailyExecutor;

@SpringBootApplication
public class AirInfoServerApplication {

//	@Autowired
//	private static DailyExecutor de;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AirInfoServerApplication.class, args);
        context.getBean(DailyExecutor.class).startExecutionAt(0,0,0); // <-- here

//		System.out.println(de);
//		de.startExecutionAt(0, 0, 0);
	}

}
