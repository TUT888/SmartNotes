package com.be08.smart_notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.be08.smart_notes.model.Quiz;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Optional<Quiz> findByIdAndQuizSetUserId(int id, int userId);
}
