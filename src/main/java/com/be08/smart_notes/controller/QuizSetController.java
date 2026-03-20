package com.be08.smart_notes.controller;

import com.be08.smart_notes.common.DefaultConstants;
import com.be08.smart_notes.dto.filter.BasicFilterDTO;
import com.be08.smart_notes.dto.request.QuizSetUpsertRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.dto.response.QuizSetResponse;
import com.be08.smart_notes.dto.view.QuizView;
import com.be08.smart_notes.enums.OriginType;
import com.be08.smart_notes.service.QuizSetService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz-sets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quiz Sets", description = "All operations for quiz sets")
public class QuizSetController {
    QuizSetService quizSetService;

    @PostMapping
    @JsonView(QuizView.Detail.class)
    @Operation(summary = "Create new quiz set")
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
    @Operation(summary = "Get all quiz sets")
    public ResponseEntity<Object> getAllQuizSets(
            @ModelAttribute BasicFilterDTO filterDTO,
            @RequestParam(required = false, defaultValue = DefaultConstants.SORT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = DefaultConstants.SORT_ORDER) String sortOrder,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_SIZE) int size) {
        PageResponse<QuizSetResponse> quizSetResponseList = quizSetService.getAllQuizSets(filterDTO, sortBy, sortOrder, page, size);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set fetched successfully")
                .data(quizSetResponseList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/default")
    @JsonView(QuizView.Detail.class)
    @Operation(summary = "Get default quiz set and its quizzes")
    public ResponseEntity<Object> getDefaultQuizSet() {
        QuizSetResponse quizSetResponse = quizSetService.getDefaultSet();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set fetched successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}")
    @JsonView(QuizView.Basic.class)
    @Operation(summary = "Get quiz set")
    public ResponseEntity<Object> getQuizSet(@PathVariable int id) {
        QuizSetResponse quizSetResponse = quizSetService.getQuizSetById(id, false);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set fetched successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{id}/quizzes")
    @JsonView(QuizView.Detail.class)
    @Operation(summary = "Get quiz set and its quizzes")
    public ResponseEntity<Object> getQuizSetWithQuizzes(@PathVariable int id) {
        QuizSetResponse quizSetResponse = quizSetService.getQuizSetById(id, true);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set fetched successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{id}")
    @JsonView(QuizView.Detail.class)
    @Operation(summary = "Update quiz set information")
    public ResponseEntity<Object> updateQuizSet(@PathVariable int id, @RequestBody QuizSetUpsertRequest request) {
        QuizSetResponse quizSetResponse = quizSetService.updateQuizSet(id, request);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set updated successfully")
                .data(quizSetResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping
    @Operation(summary = "Delete all quiz sets")
    public ResponseEntity<Object> deleteAllQuizSet() {
        quizSetService.deleteAllQuizSet();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("All quiz set deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quiz set")
    public ResponseEntity<Object> deleteQuizSet(@PathVariable int id) {
        quizSetService.deleteQuizSetById(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Quiz set deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
