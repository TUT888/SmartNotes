package com.be08.smart_notes.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AIInferenceRequest {
    // Required
    private String model;
    private RequestMessage[] messages;

    // Optional
    private double temperature;
    private double top_p;
    private String guided_json;

    // Static nested class
    @Data
    @AllArgsConstructor
    public static class RequestMessage {
        private String role;
        private String content;
    }
}
