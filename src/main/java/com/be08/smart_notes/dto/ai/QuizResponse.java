package com.be08.smart_notes.dto.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
    @NotBlank
	private String topic;
    @NotNull
    private List<Question> questions;

    @Data
    @AllArgsConstructor
    public static class Question {
        @NotBlank
        private String question;

        @NotNull
        @Size(min = 4, max = 4)
        private String[] options;

        private int correctIndex;
    }
}
