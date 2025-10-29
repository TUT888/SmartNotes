package com.be08.smart_notes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlashcardCreationRequest {
    Integer flashcardSetId;

    @NotBlank(message = "FLASHCARD_FRONT_CONTENT_REQUIRED")
    String frontContent;

    @NotBlank(message = "FLASHCARD_BACK_CONTENT_REQUIRED")
    String backContent;

    @NotNull(message = "FLASHCARD_SOURCE_DOCUMENT_ID_REQUIRED")
    Integer sourceDocumentId;
}
