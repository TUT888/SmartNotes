package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.FlashcardCreationRequest;
import com.be08.smart_notes.dto.response.FlashcardResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.FlashcardMapper;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.model.Flashcard;
import com.be08.smart_notes.model.FlashcardSet;
import com.be08.smart_notes.repository.FlashcardRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FlashcardService {
    FlashcardRepository flashcardRepository;
    FlashcardMapper flashcardMapper;

    DocumentService documentService;
    FlashcardSetService flashcardSetService;

    // -- CRUD Operations --
    // -- Create Flashcard --
    /**
     * Create a new flashcard
     * @param request FlashcardCreationRequest
     * @return FlashcardResponse
     */
    public FlashcardResponse createFlashcard(FlashcardCreationRequest request){
        // Get current user ID from claim sub of JWT token (security context)
        int currentUserId = getCurrentUserId();

        Document sourceDocument = validateAndGetSourceDocument(request.getSourceDocumentId(), currentUserId);
        FlashcardSet flashcardSet = validateAndGetFlashcardSet(request.getFlashcardSetId(), currentUserId);

        Flashcard flashcard = flashcardMapper.toFlashcard(request);
        flashcard.setSourceDocument(sourceDocument);
        flashcard.setFlashcardSet(flashcardSet);

        flashcard = flashcardRepository.save(flashcard);
        return flashcardMapper.toFlashcardResponse(flashcard);
    }

    // -- Read Flashcard --
    /**
     * Get a flashcard by its ID
     * @param flashcardId ID of the flashcard
     * @return FlashcardResponse
     */
    public FlashcardResponse getFlashcardById(int flashcardId){
        int currentUserId = getCurrentUserId();

        Flashcard flashcard = flashcardRepository.findByIdAndFlashcardSet_Owner_Id(flashcardId, currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.FLASHCARD_NOT_FOUND));

        return flashcardMapper.toFlashcardResponse(flashcard);
    }

    /**
     * Get all flashcards in a specific flashcard set
     * @param flashcardSetId ID of the flashcard set
     * @return List of FlashcardResponse
     */
    public List<FlashcardResponse> getFlashcardsBySetId(int flashcardSetId){
        int currentUserId = getCurrentUserId();

        // Validate ownership of the flashcard set
        flashcardSetService.validateOwner(flashcardSetId, currentUserId);

        List<Flashcard> flashcards = flashcardRepository.findByFlashcardSet_Id(flashcardSetId);

        return flashcards.stream()
                .map(flashcardMapper::toFlashcardResponse)
                .collect(Collectors.toList());
    }

    // -- Update Flashcard --
    /**
     * Update an existing flashcard
     * @param flashcardId ID of the flashcard to update
     * @param request Update request data
     * @return Updated FlashcardResponse
     */
    public FlashcardResponse updateFlashcard(int flashcardId, FlashcardCreationRequest request){
        int currentUserId = getCurrentUserId();

        Flashcard existingFlashcard = flashcardRepository.findByIdAndFlashcardSet_Owner_Id(flashcardId, currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.FLASHCARD_NOT_FOUND));

        if(existingFlashcard.getSourceDocument().getId() != request.getSourceDocumentId()){
            Document newSourceDocument = validateAndGetSourceDocument(request.getSourceDocumentId(), currentUserId);
            existingFlashcard.setSourceDocument(newSourceDocument);
        }

        if(request.getFlashcardSetId() != null && existingFlashcard.getFlashcardSet().getId() != request.getFlashcardSetId()){
            FlashcardSet newFlashcardSet = validateAndGetFlashcardSet(request.getFlashcardSetId(), currentUserId);
            existingFlashcard.setFlashcardSet(newFlashcardSet);
        }

        flashcardMapper.updateFlashcard(existingFlashcard, request);

        existingFlashcard = flashcardRepository.save(existingFlashcard);
        return flashcardMapper.toFlashcardResponse(existingFlashcard);
    }

    // -- Delete Flashcard --
    /**
     * Delete a flashcard by ID
     * @param flashcardId ID of the flashcard to delete
     */
    public void deleteFlashcard(int flashcardId){
        int currentUserId = getCurrentUserId();
        Flashcard flashcard = flashcardRepository.findByIdAndFlashcardSet_Owner_Id(flashcardId, currentUserId)
                .orElseThrow(() -> new AppException(ErrorCode.FLASHCARD_NOT_FOUND));
    }

    // -- Private Helper Methods --

    /**
     * Get the currently authenticated user's ID
     * @return userId
     */
    private int getCurrentUserId(){
        // Extract user ID from security context
        String userIdString = SecurityContextHolder.getContext().getAuthentication().getName();
        return Integer.parseInt(userIdString);
    }

    /**
     * Validate and get the source document
     * @param requestedDocumentId
     * @param userId
     * @return Document
     */
    private Document validateAndGetSourceDocument(Integer requestedDocumentId, int userId){
        if(requestedDocumentId != null){
            // If a specific document ID is provided, validate ownership and existence
            // and return that document
            return documentService.getDocumentIfOwned(requestedDocumentId, userId);
        }
        // If no document ID is provided, return the system source document
        return documentService.getSystemSourceDocument(userId);
    }

    /**
     * Validate and get the flashcard set
     * @param requestedSetId
     * @param userId
     * @return FlashcardSet
     */
    private FlashcardSet validateAndGetFlashcardSet(Integer requestedSetId, int userId){
        if(requestedSetId != null){
            // If a specific flashcard set ID is provided, validate ownership and existence
            // and return that flashcard set
            return flashcardSetService.validateOwner(requestedSetId, userId);
        }
        // If no flashcard set ID is provided, return or create the default flashcard set
        return flashcardSetService.getOrCreateDefaultSet(userId);
    }
}
