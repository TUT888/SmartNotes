package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.QuizGenerationRequest;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.validation.group.MultipleDocument;
import com.be08.smart_notes.validation.group.SingleDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.service.ai.QuizGenerationService;

@RestController
@RequestMapping("/api/ai/generation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "AI Generation Features", description = "Operation for AI-related features")
public class AIGenerationController {
	QuizGenerationService quizGenerationService;

	@GetMapping("/quiz-sets/sample")
    @Operation(summary = "Generate sample quiz set")
	public ResponseEntity<Object> generateSampleQuiz() {
		QuizResponse quizResponse = quizGenerationService.generateSampleQuiz();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Sample quiz set successfully generated")
                .data(quizResponse)
                .build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

    @PostMapping("/quiz-sets/default")
    @Operation(summary = "Generate single quiz based given request, stored in default quiz set")
    public ResponseEntity<Object> generateQuiz(@Validated(SingleDocument.class) @RequestBody QuizGenerationRequest quizGenerationRequest) {
        QuizResponse quizSetResponse = quizGenerationService.generateQuiz(quizGenerationRequest);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz successfully generated and added to default set")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/quiz-sets")
    @Operation(summary = "Generate multiple quizzes based given request, grouped in new quiz set")
    public ResponseEntity<Object> generateQuizSet(@Validated(MultipleDocument.class) @RequestBody QuizGenerationRequest quizGenerationRequest) {
        QuizSetResponse quizSetResponse = quizGenerationService.generateQuizSet(quizGenerationRequest);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set successfully generated")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
