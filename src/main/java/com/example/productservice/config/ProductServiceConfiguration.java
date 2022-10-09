package com.example.productservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ProductServiceConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {

        RestTemplate restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000)).build();
        return restTemplate;
    }
}
