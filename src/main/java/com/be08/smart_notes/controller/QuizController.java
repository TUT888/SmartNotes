package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.QuizDTO;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.view.Level;
import com.be08.smart_notes.service.QuizService;
import com.be08.smart_notes.validation.group.OnCreate;
import com.be08.smart_notes.validation.group.OnUpdate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizController {
    QuizService quizService;

    @PostMapping
    @JsonView(Level.Detail.class)
    public ResponseEntity<Object> createQuiz(@RequestBody @Validated(OnCreate.class) QuizDTO request) {
        QuizResponse quizResponse = quizService.createQuiz(request);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz created successfully in default set")
                .data(quizResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    @JsonView(Level.Detail.class)
    public ResponseEntity<Object> getQuiz(@PathVariable int id) {
        QuizResponse quizResponse = quizService.getQuizById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz fetched successfully")
                .data(quizResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}")
    @JsonView(Level.Detail.class)
    public ResponseEntity<Object> updateQuiz(@PathVariable int id, @Validated(OnUpdate.class) @RequestBody QuizDTO request) {
        QuizResponse quizResponse = quizService.updateQuiz(id, request);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz updated successfully")
                .data(quizResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuiz(@PathVariable int id) {
        quizService.deleteQuizById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
