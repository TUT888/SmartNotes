package com.be08.smart_notes.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.api")
public class AIInferenceProperties {
    // Authentication
    private String url;
    private String token;

    // Inference
    private String model;
    private String userRole;
    private String systemRole;
    private Double temperature;
    private Double topP;

    public boolean isMissingCredentials() {
        return url.isEmpty() || token.isEmpty();
    }

    public boolean isMissingModelConfig() {
        return model.isEmpty() || userRole.isEmpty() || systemRole.isEmpty()
                || temperature == null || topP == null;
    }
}