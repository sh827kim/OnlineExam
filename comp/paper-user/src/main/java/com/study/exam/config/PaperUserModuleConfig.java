package com.study.exam.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.study.exam.user")
@EnableJpaRepositories(basePackages = {
        "com.study.exam.user.repository"
})
@EntityScan(basePackages = {
        "com.study.exam.user.domain"
})
public class PaperUserModuleConfig {
}
