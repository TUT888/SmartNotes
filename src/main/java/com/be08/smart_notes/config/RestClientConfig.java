package com.be08.smart_notes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${API_TOKEN}")
    private String AI_API_TOKEN;

    @Value("${API_URL}")
    private String AI_API_URL;

    @Bean
    public RestClient nebiusRestClient() {
        return RestClient.builder()
                .baseUrl(AI_API_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + AI_API_TOKEN)
                .build();
    }
}
