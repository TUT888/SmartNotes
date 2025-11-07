package com.be08.smart_notes.dto;

import com.be08.smart_notes.validation.group.OnCreate;
import com.be08.smart_notes.validation.group.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizUpsertDTO {
    private Integer sourceDocumentId;

    // If null in creation request, new quiz will be categorised in default set
    @NotNull(groups = OnUpdate.class, message = "QUIZ_SET_ID_REQUIRED")
    private Integer quizSetId;

    @NotEmpty(groups = {OnCreate.class, OnUpdate.class}, message = "QUIZ_TITLE_REQUIRED")
    @JsonProperty(value = "topic")
    private String title;

    @NotEmpty(groups = OnCreate.class)
    @JsonProperty(value = "questions")
    private List<Question> questions;

    // Static nested class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Question {
        @NotNull
        @JsonProperty(value = "question")
        private String questionText;

        @NotNull
        @Size(min = 4, max = 4)
        @JsonProperty(value = "options")
        private String[] options;

        @NotNull
        @Min(0)
        @Max(3)
        @JsonProperty(value = "correct_index")
        private Integer correctIndex;
    }
}
