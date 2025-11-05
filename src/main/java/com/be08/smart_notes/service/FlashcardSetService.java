package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.FlashcardSetCreationRequest;
import com.be08.smart_notes.dto.response.FlashcardSetResponse;
import com.be08.smart_notes.enums.FlashcardSetOriginType;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.FlashcardSetMapper;
import com.be08.smart_notes.model.FlashcardSet;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.repository.FlashcardSetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class FlashcardSetService {
    FlashcardSetRepository flashcardSetRepository;
    UserService userService;

    AuthorizationService authorizationService;
    FlashcardSetMapper flashcardSetMapper;

    private static final String DEFAULT_SET_TITLE = "Unsorted Flashcards";

    public FlashcardSet validateOwner(int flashcardSetId, int userId){
        return flashcardSetRepository.findByIdAndOwner_Id(flashcardSetId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.FLASHCARD_SET_NOT_FOUND));
    }

    public FlashcardSet getOrCreateDefaultSet(int userId){
        return flashcardSetRepository.findByOwner_IdAndOriginType(userId, FlashcardSetOriginType.DEFAULT)
                .orElseGet(() -> {
                    User user = userService.getById(userId);

                    FlashcardSet defaultSet = FlashcardSet.builder()
                            .title(DEFAULT_SET_TITLE)
                            .owner(user)
                            .originType(FlashcardSetOriginType.DEFAULT)
                            .build();

                    return flashcardSetRepository.save(defaultSet);
                });
    }

    public FlashcardSetResponse createFlashcardSet(FlashcardSetCreationRequest request){
        int currentUserId = authorizationService.getCurrentUserId();
        User user = userService.getById(currentUserId);

        if(DEFAULT_SET_TITLE.equals(request.getTitle())){
            log.error("Flashcard Set Creation Failed: Title '{}' is reserved for default set", DEFAULT_SET_TITLE);
            throw new AppException(ErrorCode.INVALID_FLASHCARD_SET_TITLE);
        }

        FlashcardSet newSet = flashcardSetMapper.toFlashcardSet(request);
        newSet.setOwner(user);
        newSet.setOriginType(FlashcardSetOriginType.USER);

        newSet = flashcardSetRepository.save(newSet);
        return flashcardSetMapper.toFlashcardSetResponse(newSet);
    }

    public List<FlashcardSetResponse> getAllFlashcardSets(){
        int currentUserId = authorizationService.getCurrentUserId();

        List<FlashcardSet> flashcardSetList = flashcardSetRepository.findAllByOwner_Id(currentUserId);
        return flashcardSetList.stream()
                .filter(set -> set.getOriginType() != FlashcardSetOriginType.DEFAULT)
                .map(flashcardSetMapper::toFlashcardSetResponse)
                .collect(Collectors.toList());
    }

    public FlashcardSetResponse updateFlashcardSet(int flashcardSetId, FlashcardSetCreationRequest request){
        int currentUserId = authorizationService.getCurrentUserId();
        FlashcardSet existingSet = validateOwner(flashcardSetId, currentUserId);

        if(existingSet.getOriginType() == FlashcardSetOriginType.DEFAULT){
            log.error("Flashcard Set Update Failed: Cannot update default flashcard set with id {}", flashcardSetId);
            throw new AppException(ErrorCode.FLASHCARD_SET_CANNOT_BE_MODIFIED);
        }

        flashcardSetMapper.updateFlashcardSet(existingSet, request);

        existingSet = flashcardSetRepository.save(existingSet);
        return flashcardSetMapper.toFlashcardSetResponse(existingSet);
    }

    public void deleteFlashcardSet(int flashcardSetId){
        int currentUserId = authorizationService.getCurrentUserId();
        FlashcardSet existingSet = validateOwner(flashcardSetId, currentUserId);

        if(existingSet.getOriginType() == FlashcardSetOriginType.DEFAULT){
            log.error("Flashcard Set Deletion Failed: Cannot delete default flashcard set with id {}", flashcardSetId);
            throw new AppException(ErrorCode.FLASHCARD_SET_CANNOT_BE_DELETED);
        }

        flashcardSetRepository.deleteById(flashcardSetId);
    }
}
