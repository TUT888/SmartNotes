package com.be08.smart_notes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSetResponse {
    private Integer id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QuizResponse> quizzes;
}