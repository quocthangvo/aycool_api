package com.example.shopapp_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling  // Bật tính năng lập lịch
public class ShopappApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopappApiApplication.class, args);
    }

}
