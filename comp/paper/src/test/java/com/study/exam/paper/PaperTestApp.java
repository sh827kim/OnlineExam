package com.study.exam.paper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.study.exam.config"
})
public class PaperTestApp {

    public static void main(String[] args) {
        SpringApplication.run(PaperTestApp.class, args);
    }

    @Configuration
    @EntityScan(basePackages = {
            "com.study.exam.user.domain",
            "com.study.exam.paper.domain"
    })
    @EnableJpaRepositories(basePackages = {
            "com.study.exam.user.repository",
            "com.study.exam.paper.repository"
    })
    class JpaConfig {

    }
}
