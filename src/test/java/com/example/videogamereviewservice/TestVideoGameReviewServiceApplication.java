package com.example.videogamereviewservice;

import org.springframework.boot.SpringApplication;

public class TestVideoGameReviewServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(VideoGameReviewServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
