package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.QuizGenerationRequest;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.validation.group.MultipleDocument;
import com.be08.smart_notes.validation.group.SingleDocument;
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
public class AIGenerationController {
	QuizGenerationService quizGenerationService;

	@GetMapping("/quiz-sets/sample")
	public ResponseEntity<Object> generateSampleQuizSet() {
		QuizResponse quizResponse = quizGenerationService.generateSampleQuiz();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Sample quiz set successfully generated")
                .data(quizResponse)
                .build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

    @PostMapping("/quiz-sets/default")
    public ResponseEntity<Object> generateQuiz(@Validated(SingleDocument.class) @RequestBody QuizGenerationRequest quizGenerationRequest) {
        QuizResponse quizSetResponse = quizGenerationService.generateQuiz(quizGenerationRequest);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz successfully generated and added to default set")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/quiz-sets")
    public ResponseEntity<Object> generateQuizSet(@Validated(MultipleDocument.class) @RequestBody QuizGenerationRequest quizGenerationRequest) {
        QuizSetResponse quizSetResponse = quizGenerationService.generateQuizSet(quizGenerationRequest);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set successfully generated")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
