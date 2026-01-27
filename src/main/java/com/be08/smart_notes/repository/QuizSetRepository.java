package com.be08.smart_notes.repository;

import com.be08.smart_notes.enums.OriginType;
import com.be08.smart_notes.model.QuizSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface QuizSetRepository extends JpaRepository<QuizSet,Integer>, JpaSpecificationExecutor<QuizSet> {
    Optional<QuizSet> findByUserIdAndOriginType(int userID, OriginType originType);
    Optional<QuizSet> findByIdAndUserId(int quizSetId, int userId);

    List<QuizSet> findAllByUserId(int userId);
    Page<QuizSet> findAllByUserId(int userId, Pageable pageable);
    void deleteAllByUserId(int userId);
}
