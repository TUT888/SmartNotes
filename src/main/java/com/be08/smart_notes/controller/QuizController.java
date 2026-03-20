package com.be08.smart_notes.controller;

import com.be08.smart_notes.common.DefaultConstants;
import com.be08.smart_notes.dto.QuizUpsertDTO;
import com.be08.smart_notes.dto.filter.QuizFilterDTO;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.dto.response.QuizResponse;
import com.be08.smart_notes.dto.view.QuizView;
import com.be08.smart_notes.service.QuizService;
import com.be08.smart_notes.validation.group.OnCreate;
import com.be08.smart_notes.validation.group.OnUpdate;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quizzes", description = "All operations for quizzes")
@SecurityRequirement(name = "Bearer Authentication")
public class QuizController {
    QuizService quizService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(QuizView.Detail.class)
    @Operation(summary = "Create new quiz with associated questions")
    public ApiResponse<QuizResponse> createQuiz(@RequestBody @Validated(OnCreate.class) QuizUpsertDTO request) {
        QuizResponse quizResponse = quizService.createQuiz(request);
        return ApiResponse.<QuizResponse>builder()
                .message("Quiz created successfully in default set")
                .data(quizResponse)
                .build();
    }

    @GetMapping
    @JsonView(QuizView.Basic.class)
    @Operation(summary = "Get all quizzes by page")
    public ApiResponse<PageResponse<QuizResponse>> getAllQuizzes(
            @ModelAttribute QuizFilterDTO filterDTO,
            @RequestParam(required = false, defaultValue = DefaultConstants.SORT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = DefaultConstants.SORT_ORDER) String sortOrder,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_SIZE) int size
    ) {
        PageResponse<QuizResponse> quizResponseList = quizService.getAllQuizzes(filterDTO, sortBy, sortOrder, page, size);
        return ApiResponse.<PageResponse<QuizResponse>>builder()
                .message("Quizzes fetched successfully")
                .data(quizResponseList)
                .build();
    }

    @GetMapping("/{id}")
    @JsonView(QuizView.Detail.class)
    @Operation(summary = "Get quiz and all of its questions")
    public ApiResponse<QuizResponse> getQuiz(@PathVariable int id) {
        QuizResponse quizResponse = quizService.getQuizById(id);
        return ApiResponse.<QuizResponse>builder()
                .message("Quiz fetched successfully")
                .data(quizResponse)
                .build();
    }

    @PatchMapping("/{id}")
    @JsonView(QuizView.Basic.class)
    @Operation(summary = "Update quiz information")
    public ApiResponse<QuizResponse> updateQuiz(@PathVariable int id, @Validated(OnUpdate.class) @RequestBody QuizUpsertDTO request) {
        QuizResponse quizResponse = quizService.updateQuiz(id, request);
        return ApiResponse.<QuizResponse>builder()
                .message("Quiz updated successfully")
                .data(quizResponse)
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quiz")
    public ApiResponse<Void> deleteQuiz(@PathVariable int id) {
        quizService.deleteQuizById(id);
        return ApiResponse.<Void>builder()
                .message("Quiz set deleted successfully")
                .build();
    }
}
