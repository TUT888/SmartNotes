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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quiz Attempts", description = "Operation for quiz attempts")
@SecurityRequirement(name = "Bearer Authentication")
public class AttemptController {
    AttemptService attemptService;

    @PostMapping("/{quizId}/attempts")
    @JsonView(AttemptView.Detail.class)
    @Operation(summary = "Create new attempt for target quiz")
    public ApiResponse<AttemptResponse> createAttempt(@PathVariable int quizId) {
        AttemptResponse attemptResponse = attemptService.createNewAttempt(quizId);
        return ApiResponse.<AttemptResponse>builder()
                .message("Attempt created successfully")
                .data(attemptResponse)
                .build();
    }

    @GetMapping("/{quizId}/attempts")
    @JsonView(AttemptView.Basic.class)
    @Operation(summary = "Get all attempts by page")
    public ApiResponse<PageResponse<AttemptResponse>> getAllAttemptsForQuiz(@PathVariable int quizId,
                                                        @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_NUMBER) int page,
                                                        @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_SIZE) int size) {
        PageResponse<AttemptResponse> attemptResponseList = attemptService.getAllAttemptsByQuizId(quizId, page, size);
        return ApiResponse.<PageResponse<AttemptResponse>>builder()
                .message("All attempts for quiz fetched successfully")
                .data(attemptResponseList)
                .build();
    }

    @GetMapping("/{quizId}/attempts/{attemptId}")
    @JsonView(AttemptView.Detail.class)
    @Operation(summary = "Get attempt and its recorded answers")
    public ApiResponse<AttemptResponse> getAttempt(@PathVariable int quizId, @PathVariable int attemptId) {
        AttemptResponse attemptResponseList = attemptService.getAttemptByIdAndQuizId(quizId, attemptId);
        return ApiResponse.<AttemptResponse>builder()
                .message("Attempt fetched successfully")
                .data(attemptResponseList)
                .build();
    }

    @GetMapping("/{quizId}/attempts/{attemptId}/answer")
    @JsonView(AttemptView.Answer.class)
    @Operation(summary = "Get attempt with its recorded answers and correct answers")
    public ApiResponse<AttemptResponse> getAttemptResult(@PathVariable int quizId, @PathVariable int attemptId) {
        AttemptResponse attemptResponseList = attemptService.getAttemptByIdAndQuizId(quizId, attemptId);
        return ApiResponse.<AttemptResponse>builder()
                .message("Attempt result fetched successfully")
                .data(attemptResponseList)
                .build();
    }

    @PostMapping("/{quizId}/attempts/{attemptId}/answer")
    @JsonView(AttemptView.Answer.class)
    @Operation(summary = "Finish attempt and calculate result")
    public ApiResponse<AttemptResponse> finishAttempt(@PathVariable int quizId, @PathVariable int attemptId) {
        AttemptResponse attemptResponseList = attemptService.calculateAttemptResult(quizId, attemptId);
        return ApiResponse.<AttemptResponse>builder()
                .message("Attempt result calculated successfully")
                .data(attemptResponseList)
                .build();
    }

    @PatchMapping("/{quizId}/attempts/{attemptId}")
    @JsonView(AttemptView.Answer.class)
    @Operation(summary = "Update attempt detail and/or record answer")
    public ApiResponse<AttemptResponse.Detail> updateAttemptDetail(@PathVariable int quizId, @PathVariable int attemptId, @Valid @RequestBody AttemptDetailUpdateRequest request) {
        AttemptResponse.Detail attemptDetailResponse = attemptService.updateAttemptDetail(quizId, attemptId, request);
        return ApiResponse.<AttemptResponse.Detail>builder()
                .message("Attempt detail updated successfully")
                .data(attemptDetailResponse)
                .build();
    }

    @DeleteMapping("/{quizId}/attempts/{attemptId}")
    @Operation(summary = "Delete attempt")
    public ApiResponse<Void> deleteAttempt(@PathVariable int quizId, @PathVariable int attemptId) {
        attemptService.deleteAttemptByIdAndQuizId(quizId, attemptId);
        return ApiResponse.<Void>builder()
                .message("Attempt deleted successfully")
                .build();
    }
}
