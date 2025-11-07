package com.be08.smart_notes.dto.response;

import com.be08.smart_notes.dto.view.Level;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizSetResponse {
    @JsonView(Level.Basic.class)
    Integer id;

    @JsonView(Level.Basic.class)
    String title;

    @JsonView(Level.Basic.class)
    LocalDateTime createdAt;

    @JsonView(Level.Basic.class)
    LocalDateTime updatedAt;

    @JsonView(Level.Detail.class)
    List<QuizResponse> quizzes;
}
