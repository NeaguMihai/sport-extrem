package com.mihaineagu.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.mihaineagu.data", "com.mihaineagu.web", "com.mihaineagu.service"})
@EntityScan(basePackages = {"com.mihaineagu.data.domain"})
@EnableJpaRepositories(basePackages = "com.mihaineagu.data.repository")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
