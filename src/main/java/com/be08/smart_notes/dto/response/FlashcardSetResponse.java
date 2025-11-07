package com.be08.smart_notes.dto.response;

import com.be08.smart_notes.enums.OriginType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlashcardSetResponse {
    int id;
    String title;
    int ownerId;
    OriginType originType;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}