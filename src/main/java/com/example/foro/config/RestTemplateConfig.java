package com.example.foro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // Se usa para llamar al microservicio de usuarios
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
