package com.be08.smart_notes.dto.response;

import com.be08.smart_notes.dto.view.QuizView;
import com.be08.smart_notes.enums.OriginType;
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
    @JsonView(QuizView.Basic.class)
    Integer id;

    @JsonView(QuizView.Basic.class)
    String title;

    @JsonView(QuizView.Basic.class)
    OriginType originType;

    @JsonView(QuizView.Basic.class)
    LocalDateTime createdAt;

    @JsonView(QuizView.Basic.class)
    LocalDateTime updatedAt;

    @JsonView(QuizView.Detail.class)
    List<QuizResponse> quizzes;
}
