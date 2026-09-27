package com.imooc.tripPlannerAgent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.imooc"})
public class TripPlannerApp {
    public static void main(String[] args) {
        SpringApplication.run(TripPlannerApp.class, args);
    }
}
