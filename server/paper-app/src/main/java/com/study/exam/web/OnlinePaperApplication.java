package com.study.exam.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {
        "com.study.exam.config",
        "com.study.exam.web",
        "com.study.exam.site"

})
public class OnlinePaperApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlinePaperApplication.class, args);
    }
}
