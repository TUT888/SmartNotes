package com.be08.smart_notes.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlashcardResponse {
    int id;
    String frontContent;
    String backContent;
    int flashcardSetId;
    int sourceDocumentId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}