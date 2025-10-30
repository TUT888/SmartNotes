package com.be08.smart_notes.service;

import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.model.FlashcardSet;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.repository.FlashcardSetRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FlashcardSetService {
    FlashcardSetRepository flashcardSetRepository;
    UserService userService;

    private static final String DEFAULT_SET_TITLE = "Unsorted Flashcards";

    public FlashcardSet validateOwner(int flashcardSetId, int userId){
        return flashcardSetRepository.findByIdAndOwner_Id(flashcardSetId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.FLASHCARD_SET_NOT_FOUND));
    }

    public FlashcardSet getOrCreateDefaultSet(int userId){
        return flashcardSetRepository.findByTitleAndOwner_Id(DEFAULT_SET_TITLE, userId)
                .orElseGet(() -> {
                    User user = userService.getById(userId);

                    FlashcardSet defaultSet = FlashcardSet.builder()
                            .title(DEFAULT_SET_TITLE)
                            .owner(user)
                            .build();

                    return flashcardSetRepository.save(defaultSet);
                });
    }
}
