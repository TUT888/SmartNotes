package com.be08.smart_notes.dto.response;

import com.be08.smart_notes.dto.view.AttemptView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttemptResponse {
    @JsonView(AttemptView.Basic.class)
    Integer id;

    @JsonView(AttemptView.Basic.class)
    Integer quizId;

    @JsonView(AttemptView.Basic.class)
    LocalDateTime attemptAt;

    @JsonView(AttemptView.Basic.class)
    Integer totalQuestion;

    @JsonView(AttemptView.Basic.class)
    Integer score;

    @JsonView(AttemptView.Detail.class)
    List<AttemptResponse.Detail> attemptDetails;

    // Nested static class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Detail {
        @JsonView(AttemptView.Detail.class)
        Integer id;

        // Question detail
        @JsonView(AttemptView.Detail.class)
        String questionText;

        @JsonView(AttemptView.Detail.class)
        String optionA;

        @JsonView(AttemptView.Detail.class)
        String optionB;

        @JsonView(AttemptView.Detail.class)
        String optionC;

        @JsonView(AttemptView.Detail.class)
        String optionD;

        @JsonView(AttemptView.Answer.class)
        String correctAnswer;

        // User selection
        @JsonView(AttemptView.Answer.class)
        Character userAnswer;

        @JsonView(AttemptView.Answer.class)
        Boolean isCorrect;
    }
}
