package com.be08.smart_notes.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizGenerationRequest {
    @NotEmpty
    @Size(min = 1, max = 5, message = "QUIZ_DOCUMENT_SIZE_EXCEED")
    private List<Integer> ids;

    @NotNull
    @Builder.Default
    @Min(value = 1, message = "INVALID_QUIZ_SIZE")
    @Max(value = 50, message = "INVALID_QUIZ_SIZE")
    private Integer totalQuestions = 10;
}
