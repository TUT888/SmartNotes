package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.ai.AIQuizResponse;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.service.QuizService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizController {
    QuizService quizService;

    @PostMapping
    public ResponseEntity<Object> createQuiz(@RequestBody AIQuizResponse aiQuizResponse) {
        QuizResponse quizResponse = quizService.saveQuizFromAIResponse(1, aiQuizResponse);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz created successfully")
                .data(quizResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuiz(@PathVariable int id) {
        quizService.deleteQuizById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getQuiz(@PathVariable int id) {
        QuizResponse quizResponse = quizService.getQuizById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz fetched successfully")
                .data(quizResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
