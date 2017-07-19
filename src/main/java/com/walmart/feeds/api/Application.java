package com.walmart.feeds.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by r0i001q on 19/07/17.
 */
@SpringBootApplication(scanBasePackages = "com.walmart.feeds.*")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
