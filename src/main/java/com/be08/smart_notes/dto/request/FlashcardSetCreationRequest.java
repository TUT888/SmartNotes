package com.be08.smart_notes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlashcardSetCreationRequest {
    @NotBlank(message = "FLASHCARD_SET_TITLE_REQUIRED")
    @Size(max = 100, message = "FLASHCARD_SET_TITLE_TOO_LONG")
    String title;
}