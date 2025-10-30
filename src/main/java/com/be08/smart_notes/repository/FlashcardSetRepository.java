package com.be08.smart_notes.repository;

import com.be08.smart_notes.model.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Integer> {
    /**
     * Find flashcard set by id and owner id
     * @param flashcardSetId
     * @param ownerId
     * @return Optional containing the flashcard set if found, otherwise empty
     */
    Optional<FlashcardSet> findByIdAndOwner_Id(int flashcardSetId, int ownerId);

    /**
     * Find flashcard set by title and owner id
     * @param title
     * @param ownerId
     * @return Optional containing the flashcard set if found, otherwise empty
     */
    Optional<FlashcardSet> findByTitleAndOwner_Id(String title, int ownerId);
}
