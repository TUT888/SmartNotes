package com.be08.smart_notes.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.be08.smart_notes.dto.view.Level;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    @JsonView(Level.Basic.class)
    private Integer id;

    @JsonView(Level.Basic.class)
    private String title;

    @JsonView(Level.Basic.class)
    private Integer quizSetId;

    @JsonView(Level.Basic.class)
    private Integer sourceDocumentId;

    @JsonView(Level.Basic.class)
    private LocalDateTime createdAt;

    @JsonView(Level.Basic.class)
    private LocalDateTime updatedAt;

    @JsonView(Level.Detail.class)
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
