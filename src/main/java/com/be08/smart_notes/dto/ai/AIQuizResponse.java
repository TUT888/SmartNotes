package com.be08.smart_notes.dto.ai;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIQuizResponse {
    @NotNull
    @JsonProperty(value = "topic")
	private String title;

    @NotNull
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
