package com.be08.smart_notes.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteResponse {
    Integer id;
    String title;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
