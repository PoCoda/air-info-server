package com.tondi.airinfoserver;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class AirInfoController {

    @RequestMapping("/data")
    String home() {
        return "Hello World man!";
    }

    public static void main(String[] args) {
        SpringApplication.run(AirInfoController.class, args);
    }

}