package com.be08.smart_notes.repository;

import com.be08.smart_notes.enums.OriginType;
import com.be08.smart_notes.model.FlashcardSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Integer> {
    /**
     * Find the specific flashcard set by its id and owner id
     * @param flashcardSetId
     * @param ownerId
     * @return Optional containing the flashcard set if found, otherwise empty
     */
    Optional<FlashcardSet> findByIdAndOwner_Id(int flashcardSetId, int ownerId);

    /**
     * Find the unique default flashcard set for a user by owner id and origin type
     * @param ownerId
     * @param originType
     * @return Optional containing the flashcard set if found, otherwise empty
     */
    Optional<FlashcardSet> findByOwner_IdAndOriginType(int ownerId, OriginType originType);

    /**
     * Find all flashcard sets owned by a specific user
     * @param ownerId
     * @return List of flashcard sets owned by the user
     */
    List<FlashcardSet> findAllByOwner_Id(int ownerId);

    /**
     * Find all flashcard sets owned by a specific user with a specific origin type
     * @param ownerId
     * @param originType
     * @return List of flashcard sets owned by the user with the specified origin type
     */
    List<FlashcardSet> findAllByOwner_IdAndOriginType(int ownerId, OriginType originType);
}