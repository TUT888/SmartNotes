package com.be08.smart_notes.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AIInferenceRequest {
    String model;
    RequestMessage[] messages;
    double temperature;

    @JsonProperty("top_p")
    double topP;

    @JsonProperty("guided_json")
    String guidedJson;

    // Static nested class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class RequestMessage {
        String role;
        String content;
    }
}
