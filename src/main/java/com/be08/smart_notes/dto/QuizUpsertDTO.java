package com.be08.smart_notes.dto;

import com.be08.smart_notes.validation.group.OnCreate;
import com.be08.smart_notes.validation.group.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizUpsertDTO {
    Integer sourceDocumentId;

    // If null in creation request, new quiz will be categorised in default set
    @NotNull(groups = OnUpdate.class, message = "QUIZ_SET_ID_REQUIRED")
    Integer quizSetId;

    @NotEmpty(groups = {OnCreate.class, OnUpdate.class}, message = "QUIZ_TITLE_REQUIRED")
    @JsonProperty(value = "topic")
    String title;

    // Questions are ignored in mapper, will not modify questions in quiz update request (currently not supported)
    @NotEmpty(groups = OnCreate.class, message = "QUIZ_QUESTION_REQUIRED")
    @JsonProperty(value = "questions")
    List<Question> questions;

    // Static nested class
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Question {
        @NotNull
        @JsonProperty(value = "question")
        String questionText;

        @NotNull
        @Size(min = 4, max = 4)
        @JsonProperty(value = "options")
        String[] options;

        @NotNull
        @Min(0)
        @Max(3)
        @JsonProperty(value = "correct_index")
        Integer correctIndex;
    }
}
