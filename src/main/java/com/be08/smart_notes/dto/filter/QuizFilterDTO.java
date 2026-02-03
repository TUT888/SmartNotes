package com.be08.smart_notes.dto.filter;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizFilterDTO extends BasicFilterDTO{
    Integer quizSetId;
}
