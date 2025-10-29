package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.ai.QuizResponse;
import com.be08.smart_notes.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.service.ai.QuizGenerationService;

@RestController
@RequestMapping("/api/ai/quiz/")
public class AIController {
	@Autowired
	private QuizGenerationService quizGenerationService;

	@GetMapping("/generate/sample")
	public ResponseEntity<Object> generateSampleQuiz() {
        int userId = 1;
		QuizResponse quizResponse = quizGenerationService.generateSampleQuiz(userId);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Sample quiz generated created successfully")
                .data(quizResponse)
                .build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@GetMapping("/generate/{noteId}")
	public ResponseEntity<Object> generateQuiz(@PathVariable int noteId) {
        int userId = 1;
		QuizResponse quizResponse = quizGenerationService.generateQuizFromSingleNote(userId, noteId);
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
