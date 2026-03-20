package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.FlashcardCreationRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.FlashcardResponse;
import com.be08.smart_notes.service.FlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Flashcards", description = "Operation for flashcards")
@SecurityRequirement(name = "Bearer Authentication")
public class FlashcardController {
    FlashcardService flashcardService;

    @PostMapping
    @Operation(summary = "Create new flashcard")
    public ApiResponse<FlashcardResponse> createFlashcard(@RequestBody FlashcardCreationRequest request){
        FlashcardResponse response = flashcardService.createFlashcard(request);

        return ApiResponse.<FlashcardResponse>builder()
                .message("Flashcard created successfully!")
                .data(response)
                .build();
    }

    @GetMapping("/{flashcardId}")
    @Operation(summary = "Create flashcard")
    public ApiResponse<FlashcardResponse> getFlashcard(@PathVariable int flashcardId){
        FlashcardResponse response = flashcardService.getFlashcardById(flashcardId);

        return ApiResponse.<FlashcardResponse>builder()
                .message("Flashcard retrieved successfully!")
                .data(response)
                .build();
    }

    @PutMapping("/{flashcardId}")
    @Operation(summary = "Update flashcard")
    public ApiResponse<FlashcardResponse> updateFlashcard(@PathVariable int flashcardId, @RequestBody @Valid FlashcardCreationRequest request){
        FlashcardResponse response = flashcardService.updateFlashcard(flashcardId, request);

        return ApiResponse.<FlashcardResponse>builder()
                .message("Flashcard updated successfully!")
                .data(response)
                .build();
    }

    @DeleteMapping("/{flashcardId}")
    @Operation(summary = "Delete flashcard")
    public ApiResponse<Void> deleteFlashcard(@PathVariable int flashcardId){
        flashcardService.deleteFlashcard(flashcardId);

        return ApiResponse.<Void>builder()
                .message("Flashcard deleted successfully!")
                .build();
    }
}