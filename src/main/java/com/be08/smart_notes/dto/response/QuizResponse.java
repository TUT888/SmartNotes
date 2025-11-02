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
    @NotNull
    private Integer id;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
	private String title;

    @NotNull
    private List<Question> questions;

    // Static nested class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        @NotNull
        private Integer id;

        @NotNull
        private String questionText;

        @NotNull
        private String optionA;

        @NotNull
        private String optionB;

        @NotNull
        private String optionC;

        @NotNull
        private String optionD;

        @NotNull
        private String correctAnswer;

        @NotNull
        private Integer sourceDocumentId;
    }
}
