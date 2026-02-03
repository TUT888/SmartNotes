package com.be08.smart_notes.repository;

import com.be08.smart_notes.model.Attempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Integer> {
    Optional<Attempt> findByIdAndQuiz_QuizSet_UserId(int id, int userId);

    List<Attempt> findByQuizIdAndQuiz_QuizSet_UserId(int quizId, int userId);
    Page<Attempt> findByQuizIdAndQuiz_QuizSet_UserId(int quizId, int userId, Pageable pageable);
}
