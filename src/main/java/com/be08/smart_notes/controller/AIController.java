package com.be08.smart_notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.be08.smart_notes.dto.ai.QuizResponse;
import com.be08.smart_notes.service.ai.QuizGenerationService;

@RestController
@RequestMapping("/api/ai")
public class AIController {
	@Autowired
	private QuizGenerationService quizGenerationService;
	
	@GetMapping("/generateQuiz/sample")
	public ResponseEntity<Object> generateSampleQuiz() {
		QuizResponse quizList = quizGenerationService.generateSampleQuiz();
		return ResponseEntity.status(HttpStatus.OK).body(quizList);
	}
	
	@GetMapping("/generateQuiz/{noteId}")
	public ResponseEntity<Object> generateQuiz(@PathVariable Long noteId) {
		QuizResponse quizList = quizGenerationService.generateQuizFromNote(noteId);
		return ResponseEntity.status(HttpStatus.OK).body(quizList);
	}
}
