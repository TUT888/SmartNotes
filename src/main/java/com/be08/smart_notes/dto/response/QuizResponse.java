package com.be08.smart_notes.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    private Integer id;
    private String title;
    private Integer sourceDocumentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Question> questions;

    // Static nested class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        private Integer id;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String correctAnswer;
    }
}
