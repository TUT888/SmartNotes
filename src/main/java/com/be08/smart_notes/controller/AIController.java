package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.ai.QuizGenerationRequest;
import com.be08.smart_notes.dto.ai.QuizQuestion;
import com.be08.smart_notes.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.service.ai.QuizGenerationService;

import java.util.List;

@RestController
@RequestMapping("/api/ai/quiz/")
public class AIController {
	@Autowired
	private QuizGenerationService quizGenerationService;

	@GetMapping("/generate/sample")
	public ResponseEntity<Object> generateSampleQuiz() {
		QuizQuestion quizList = quizGenerationService.generateSampleQuiz();
		return ResponseEntity.status(HttpStatus.OK).body(quizList);
	}

	@GetMapping("/generate/{noteId}")
	public ResponseEntity<Object> generateQuiz(@PathVariable int noteId) {
        int userId = 1;
		QuizQuestion quizList = quizGenerationService.generateQuizFromSingleNote(userId, noteId);
		return ResponseEntity.status(HttpStatus.OK).body(quizList);
	}

//    @PostMapping("/generate")
//    public ResponseEntity<Object> generateQuizFromNotes(@RequestBody QuizGenerationRequest quizGenerationRequest) {
//        List<Document> quizList = quizGenerationService.generateQuizFromListOfNotes(quizGenerationRequest.getIds());
//        return ResponseEntity.status(HttpStatus.OK).body(quizList);
//    }
}
