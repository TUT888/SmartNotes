package com.be08.smart_notes.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class AIInferenceRequest {
    // Required
    private String model;
    private RequestMessage[] messages;

    // Optional
    private double temperature;

    @JsonProperty("top_p")
    private double topP;

    @JsonProperty("guided_json")
    private String guidedJson;

    // Static nested class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestMessage {
        private String role;
        private String content;
    }
}
