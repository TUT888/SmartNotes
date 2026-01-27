package com.be08.smart_notes.dto.filter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasicFilterDTO {
    String keyword;
    LocalDate createdFrom;
    LocalDate createdTo;
    LocalDate updatedFrom;
    LocalDate updatedTo;
}
