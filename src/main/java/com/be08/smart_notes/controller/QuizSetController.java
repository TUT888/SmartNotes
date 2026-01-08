package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.QuizSetUpsertRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.dto.view.QuizView;
import com.be08.smart_notes.enums.OriginType;
import com.be08.smart_notes.service.QuizSetService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-sets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizSetController {
    QuizSetService quizSetService;

    @PostMapping
    @JsonView(QuizView.Detail.class)
    public ResponseEntity<Object> createQuizSet(@RequestBody @Valid QuizSetUpsertRequest request) {
        QuizSetResponse quizSetResponse = quizSetService.createQuizSet(request, OriginType.USER);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set created successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping
    @JsonView(QuizView.Basic.class)
    public ResponseEntity<Object> getAllQuizSets(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "6") int size
    ) {
        PageResponse<QuizSetResponse> quizSetResponseList = quizSetService.getAllQuizSets(page, size);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set fetched successfully")
                .data(quizSetResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/default")
    @JsonView(QuizView.Detail.class)
    public ResponseEntity<Object> getDefaultQuizSet() {
        QuizSetResponse quizSetResponse = quizSetService.getDefaultSet();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set fetched successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    @JsonView(QuizView.Detail.class)
    public ResponseEntity<Object> getQuizSet(@PathVariable int id) {
        QuizSetResponse quizSetResponse = quizSetService.getQuizSetById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set fetched successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}")
    @JsonView(QuizView.Detail.class)
    public ResponseEntity<Object> updateQuizSet(@PathVariable int id, @RequestBody QuizSetUpsertRequest request) {
        QuizSetResponse quizSetResponse = quizSetService.updateQuizSet(id, request);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set updated successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAllQuizSet() {
        quizSetService.deleteAllQuizSet();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("All quiz set deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuizSet(@PathVariable int id) {
        quizSetService.deleteQuizSetById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
