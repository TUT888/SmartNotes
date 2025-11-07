package com.be08.smart_notes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteUpsertRequest {
    @NotNull
    private String title = "Untitled Note";

    @NotBlank(message = "NOTE_CONTENT_EMPTY")
    private String content;
}