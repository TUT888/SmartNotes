package com.be08.smart_notes.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestClientConfig {
    AIInferenceProperties properties;

    @Bean
    public RestClient nebiusRestClient() {
        if (properties.isMissingCredentials()) {
            log.warn("Could not create Rest Client for AI Inference, credentials are not configured");
            return RestClient.builder().build();
        }

        log.info("Successfully loaded credential configuration for AI Inference");
        return RestClient.builder()
                .baseUrl(properties.getUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + properties.getToken())
                .build();
    }
}
