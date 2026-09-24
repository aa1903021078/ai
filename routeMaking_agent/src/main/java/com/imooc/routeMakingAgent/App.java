package com.imooc.routeMakingAgent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.imooc"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
