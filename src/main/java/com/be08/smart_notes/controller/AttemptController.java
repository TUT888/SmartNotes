package com.be08.smart_notes.controller;

import com.be08.smart_notes.common.DefaultConstants;
import com.be08.smart_notes.dto.request.AttemptDetailUpdateRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.AttemptResponse;
import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.dto.view.AttemptView;
import com.be08.smart_notes.service.AttemptService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Attempts", description = "Operation for quiz attempts")
public class AttemptController {
    AttemptService attemptService;

    @PostMapping("/{quizId}/attempts")
    @JsonView(AttemptView.Detail.class)
    @Operation(summary = "Create new attempt for target quiz")
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
    @Operation(summary = "Get all attempts by page")
    public ResponseEntity<Object> getAllAttemptsForQuiz(@PathVariable int quizId,
                                                        @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_NUMBER) int page,
                                                        @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_SIZE) int size) {
        PageResponse<AttemptResponse> attemptResponseList = attemptService.getAllAttemptsByQuizId(quizId, page, size);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("All attempts for quiz fetched successfully")
                .data(attemptResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{quizId}/attempts/{attemptId}")
    @JsonView(AttemptView.Detail.class)
    @Operation(summary = "Get attempt and its recorded answers")
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
    @Operation(summary = "Get attempt with its recorded answers and correct answers")
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
    @Operation(summary = "Finish attempt and calculate result")
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
    @Operation(summary = "Update attempt detail and/or record answer")
    public ResponseEntity<Object> updateAttemptDetail(@PathVariable int quizId, @PathVariable int attemptId, @Valid @RequestBody AttemptDetailUpdateRequest request) {
        AttemptResponse.Detail attemptDetailResponse = attemptService.updateAttemptDetail(quizId, attemptId, request);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Attempt detail updated successfully")
                .data(attemptDetailResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{quizId}/attempts/{attemptId}")
    @Operation(summary = "Delete attempt")
    public ResponseEntity<Object> deleteAttempt(@PathVariable int quizId, @PathVariable int attemptId) {
        attemptService.deleteAttemptByIdAndQuizId(quizId, attemptId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Attempt deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
