package com.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookStoreApplication {

    private static final Logger log = LoggerFactory.getLogger(BookStoreApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }
}