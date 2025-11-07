package com.be08.smart_notes.dto.ai;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AIInferenceResponse {
    String id;
    String model;
    String object;
    ResponseChoice[] choices;

    // Static nested class
    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ResponseChoice {
        ResponseMessage message;

        @Data
        @AllArgsConstructor
        @FieldDefaults(level = AccessLevel.PRIVATE)
        public static class ResponseMessage {
            String content;
            String role;
        }
    }
}
