package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.QuizQuestion;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.service.QuizSetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz-sets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizSetController {
    QuizSetService quizSetService;

    @PostMapping
    public ResponseEntity<Object> createQuizSet(@RequestBody QuizQuestion quizQuestion) {
        QuizSetResponse quizSetResponse = quizSetService.saveQuizSet(quizQuestion);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set created successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAllQuizSet() {
        quizSetService.deleteAllQuizSet();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("All quiz set deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuizSet(@PathVariable int id) {
        quizSetService.deleteQuizSetById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getQuizSet(@PathVariable int id) {
        QuizSetResponse quizSetResponse = quizSetService.getQuizSetById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz fetched successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
