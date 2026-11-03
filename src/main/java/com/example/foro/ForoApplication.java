package com.example.foro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ForoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForoApplication.class, args);
    }

    // Se usa para llamar al microservicio de usuarios
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
