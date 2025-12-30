package com.be08.smart_notes.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.be08.smart_notes.dto.view.QuizView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizResponse {
    @JsonView(QuizView.Basic.class)
    Integer id;

    @JsonView(QuizView.Basic.class)
    String title;

    @JsonView(QuizView.Basic.class)
    Integer quizSetId;

    @JsonView(QuizView.Basic.class)
    Integer sourceDocumentId;

    @JsonView(QuizView.Basic.class)
    LocalDateTime createdAt;

    @JsonView(QuizView.Basic.class)
    LocalDateTime updatedAt;

    @JsonView(QuizView.Detail.class)
    List<Question> questions;

    // Static nested class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Question {
        Integer id;
        String questionText;
        String optionA;
        String optionB;
        String optionC;
        String optionD;
        String correctAnswer;
    }
}
