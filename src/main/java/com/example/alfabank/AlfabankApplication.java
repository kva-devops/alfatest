package com.example.alfabank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AlfabankApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlfabankApplication.class, args);
    }

}
