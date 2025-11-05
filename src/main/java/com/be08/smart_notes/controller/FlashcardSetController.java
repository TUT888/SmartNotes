package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.FlashcardSetCreationRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.FlashcardResponse;
import com.be08.smart_notes.dto.response.FlashcardSetResponse;
import com.be08.smart_notes.service.FlashcardService;
import com.be08.smart_notes.service.FlashcardSetService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcard-sets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FlashcardSetController {
    FlashcardSetService flashcardSetService;
    FlashcardService flashcardService;

    /**
     * Create a new flashcard set
     * @param request FlashcardSetCreationRequest
     * @return ApiResponse containing FlashcardSetResponse
     */
    @PostMapping
    public ApiResponse<FlashcardSetResponse> createFlashcardSet(@RequestBody @Valid FlashcardSetCreationRequest request){
        FlashcardSetResponse response = flashcardSetService.createFlashcardSet(request);

        return ApiResponse.<FlashcardSetResponse>builder()
                .message("Flashcard set created successfully!")
                .data(response)
                .build();
    }

    /**
     * Get all flashcard sets for the current user
     * @return ApiResponse containing list of FlashcardSetResponse
     */
    @GetMapping
    public ApiResponse<List<FlashcardSetResponse>> getAllFlashcardSets(){
        List<FlashcardSetResponse> response = flashcardSetService.getAllFlashcardSets();

        return ApiResponse.<List<FlashcardSetResponse>>builder()
                .message("Flashcard sets retrieved successfully!")
                .data(response)
                .build();
    }

    /**
     * Get a flashcard set by its ID
     * @param flashcardSetId ID of the flashcard set
     * @return ApiResponse containing FlashcardSetResponse
     */
    @GetMapping("/{flashcardSetId}")
    public ApiResponse<FlashcardSetResponse> getFlashcardSet(@PathVariable int flashcardSetId){
        FlashcardSetResponse response = flashcardSetService.getFlashcardSet(flashcardSetId);

        return ApiResponse.<FlashcardSetResponse>builder()
                .message("Flashcard set retrieved successfully!")
                .data(response)
                .build();
    }

    /**
     * Get all flashcards in a specific flashcard set
     * @param flashcardSetId ID of the flashcard set
     * @return ApiResponse containing list of FlashcardResponse
     */
    @GetMapping("/{flashcardSetId}/flashcards")
    public ApiResponse<List<FlashcardResponse>> getFlashcardsBySet(@PathVariable int flashcardSetId){
        List<FlashcardResponse> response = flashcardService.getFlashcardsBySetId(flashcardSetId);

        return ApiResponse.<List<FlashcardResponse>>builder()
                .message("Flashcards retrieved successfully!")
                .data(response)
                .build();
    }

    /**
     * Update a flashcard set
     * @param flashcardSetId ID of the flashcard set
     * @param request FlashcardSetCreationRequest
     * @return ApiResponse containing updated FlashcardSetResponse
     */
    @PutMapping("/{flashcardSetId}")
    public ApiResponse<FlashcardSetResponse> updateFlashcardSet(@PathVariable int flashcardSetId, @RequestBody @Valid FlashcardSetCreationRequest request){
        FlashcardSetResponse response = flashcardSetService.updateFlashcardSet(flashcardSetId, request);

        return ApiResponse.<FlashcardSetResponse>builder()
                .message("Flashcard set updated successfully!")
                .data(response)
                .build();
    }

    /**
     * Delete a flashcard set
     * @param flashcardSetId ID of the flashcard set
     * @return ApiResponse with deletion confirmation
     */
    @DeleteMapping("/{flashcardSetId}")
    public ApiResponse<Void> deleteFlashcardSet(@PathVariable int flashcardSetId){
        flashcardSetService.deleteFlashcardSet(flashcardSetId);

        return ApiResponse.<Void>builder()
                .message("Flashcard set deleted successfully!")
                .build();
    }
}
