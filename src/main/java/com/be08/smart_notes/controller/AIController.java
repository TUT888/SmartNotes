package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.service.ai.QuizGenerationService;

@RestController
@RequestMapping("/api/ai/quiz/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AIController {
	QuizGenerationService quizGenerationService;

	@GetMapping("/generate/sample")
	public ResponseEntity<Object> generateSampleQuiz() {
		QuizResponse quizResponse = quizGenerationService.generateSampleQuiz();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Sample quiz generated created successfully")
                .data(quizResponse)
                .build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@GetMapping("/generate/{noteId}")
	public ResponseEntity<Object> generateQuiz(@PathVariable int noteId) {
		QuizResponse quizResponse = quizGenerationService.generateQuizFromSingleNote(noteId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz generated created successfully")
                .data(quizResponse)
                .build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

//    @PostMapping("/generate")
//    public ResponseEntity<Object> generateQuizFromNotes(@RequestBody QuizGenerationRequest quizGenerationRequest) {
//        List<Document> quizList = quizGenerationService.generateQuizFromListOfNotes(quizGenerationRequest.getIds());
//        return ResponseEntity.status(HttpStatus.OK).body(quizList);
//    }
}
