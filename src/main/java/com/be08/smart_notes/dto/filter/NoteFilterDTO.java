package com.be08.smart_notes.dto.filter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteFilterDTO {
    String keyword;
    LocalDate createdFrom;
    LocalDateTime createdTo;
    LocalDateTime updatedFrom;
    LocalDateTime updatedTo;
}
