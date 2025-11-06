package com.be08.smart_notes.dto.response;

import com.be08.smart_notes.dto.view.Level;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSetResponse {
    @JsonView(Level.Basic.class)
    private Integer id;

    @JsonView(Level.Basic.class)
    private String title;

    @JsonView(Level.Basic.class)
    private LocalDateTime createdAt;

    @JsonView(Level.Basic.class)
    private LocalDateTime updatedAt;

    @JsonView(Level.Detail.class)
    private List<QuizResponse> quizzes;
}
