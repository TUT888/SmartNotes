package com.be08.smart_notes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteResponse {
    private Integer id;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}