package com.keduit.dadog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableJpaAuditing
public class AuditorConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {

        return new AuditorAwareImpl();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
