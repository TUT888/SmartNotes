package com.be08.smart_notes.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AIInferenceResponse {
    private String id;
    private String model;
    private String object;
    private ResponseChoice[] choices;

    // Static nested class
    @Data
    @AllArgsConstructor
    public static class ResponseChoice {
        private ResponseMessage message;

        @Data
        @AllArgsConstructor
        public static class ResponseMessage {
            private String content;
            private String role;
        }
    }
}
