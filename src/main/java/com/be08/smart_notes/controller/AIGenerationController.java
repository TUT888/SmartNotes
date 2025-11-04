package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.QuizGenerationRequest;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.service.ai.QuizGenerationService;

import java.util.List;

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
                .message("Sample quiz set generated created successfully")
                .data(quizResponse)
                .build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

    @PostMapping("/quiz-sets")
    public ResponseEntity<Object> generateQuizSet(@RequestBody QuizGenerationRequest quizGenerationRequest) {
        QuizSetResponse quizSetResponse = quizGenerationService.generateQuiz(quizGenerationRequest);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set generated created successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
