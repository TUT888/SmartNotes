package com.be08.smart_notes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.be08.smart_notes.model.Quiz;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Optional<Quiz> findByIdAndQuizSetUserId(int id, int userId);
    List<Quiz> findAllByQuizSetUserId(int userId);
    Page<Quiz> findAllByQuizSetUserId(int userId, Pageable pageable);
}
