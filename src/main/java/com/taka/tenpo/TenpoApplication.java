package com.taka.tenpo;

import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class TenpoApplication implements CommandLineRunner {

    private static final Logger LOGGER = getLogger(TenpoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TenpoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        LOGGER.info("TenpoApplication -> run has started...");
    }
}
