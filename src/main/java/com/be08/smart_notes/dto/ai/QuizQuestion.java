package com.be08.smart_notes.dto.ai;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion {
    @NotNull
	private String topic;
    @NotNull
    private List<Question> questions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        @NotNull
        private String question;

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
    }
}
