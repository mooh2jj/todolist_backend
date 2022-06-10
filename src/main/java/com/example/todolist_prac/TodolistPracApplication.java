package com.example.todolist_prac;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class TodolistPracApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodolistPracApplication.class, args);
    }

}
