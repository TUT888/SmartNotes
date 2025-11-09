package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.AttemptDetailUpdateRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.AttemptResponse;
import com.be08.smart_notes.dto.view.AttemptView;
import com.be08.smart_notes.service.AttemptService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttemptController {
    AttemptService attemptService;

    @PostMapping("/{quizId}/attempts")
    @JsonView(AttemptView.Detail.class)
    public ResponseEntity<Object> createAttempt(@PathVariable int quizId) {
        AttemptResponse attemptResponse = attemptService.createNewAttempt(quizId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Attempt created successfully")
                .data(attemptResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{quizId}/attempts")
    @JsonView(AttemptView.Basic.class)
    public ResponseEntity<Object> getAllAttemptsForQuiz(@PathVariable int quizId) {
        List<AttemptResponse> attemptResponseList = attemptService.getAllAttemptsByQuizId(quizId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("All attempts for quiz fetched successfully")
                .data(attemptResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{quizId}/attempts/{attemptId}")
    @JsonView(AttemptView.Detail.class)
    public ResponseEntity<Object> getAttempt(@PathVariable int quizId, @PathVariable int attemptId) {
        AttemptResponse attemptResponseList = attemptService.getAttemptByIdAndQuizId(quizId, attemptId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Attempt fetched successfully")
                .data(attemptResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{quizId}/attempts/{attemptId}/answer")
    @JsonView(AttemptView.Answer.class)
    public ResponseEntity<Object> getAttemptResult(@PathVariable int quizId, @PathVariable int attemptId) {
        AttemptResponse attemptResponseList = attemptService.getAttemptByIdAndQuizId(quizId, attemptId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Attempt result fetched successfully")
                .data(attemptResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/{quizId}/attempts/{attemptId}/answer")
    @JsonView(AttemptView.Answer.class)
    public ResponseEntity<Object> finishAttempt(@PathVariable int quizId, @PathVariable int attemptId) {
        AttemptResponse attemptResponseList = attemptService.calculateAttemptResult(quizId, attemptId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Attempt result calculated successfully")
                .data(attemptResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{quizId}/attempts/{attemptId}")
    @JsonView(AttemptView.Answer.class)
    public ResponseEntity<Object> updateAttemptDetail(@PathVariable int quizId, @PathVariable int attemptId, @Valid @RequestBody AttemptDetailUpdateRequest request) {
        AttemptResponse.Detail attemptDetailResponse = attemptService.updateAttemptDetail(quizId, attemptId, request);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Attempt detail updated successfully")
                .data(attemptDetailResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{quizId}/attempts/{attemptId}")
    public ResponseEntity<Object> deleteAttempt(@PathVariable int quizId, @PathVariable int attemptId) {
        attemptService.deleteAttemptByIdAndQuizId(quizId, attemptId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Attempt deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
