package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.ai.QuizQuestion;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.model.Quiz;
import com.be08.smart_notes.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<Object> createQuiz(@RequestBody QuizQuestion quizQuestion) {
        int userId = 1;
        Quiz quiz = quizService.createQuiz(userId, 1, quizQuestion);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz created successfully")
                .data(quiz)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuiz(@PathVariable int id) {
        quizService.deleteQuiz(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
