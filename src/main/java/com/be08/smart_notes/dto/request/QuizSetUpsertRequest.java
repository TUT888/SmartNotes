package com.be08.smart_notes.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSetUpsertRequest {
    @NotBlank(message = "QUIZ_SET_TITLE_REQUIRED")
    private String title;
}
