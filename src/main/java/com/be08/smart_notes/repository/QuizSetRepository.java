package com.be08.smart_notes.repository;

import com.be08.smart_notes.model.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizSetRepository extends JpaRepository<QuizSet,Integer> {
    Optional<QuizSet> findByTitleAndUserId(String title, int userId);
}
