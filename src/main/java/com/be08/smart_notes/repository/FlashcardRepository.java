package com.be08.smart_notes.repository;

import com.be08.smart_notes.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Integer> {
    /**
     * Find all flashcards belonging to a specific flashcard set
     * @param flashcardSetId ID of the flashcard set
     * @return List of flashcards
     */
    List<Flashcard> findByFlashcardSet_Id(int flashcardSetId);

    /**
     * Find a flashcard by its ID and the owner's user ID
     * @param flashcardId ID of the flashcard
     * @param userId ID of the flashcard owner
     * @return Optional containing the flashcard if found, otherwise empty
     */
    Optional<Flashcard> findByIdAndFlashcardSet_Owner_Id(int flashcardId, int userId);
}
