package com.be08.smart_notes.repository;

import com.be08.smart_notes.model.AttemptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttemptDetailRepository extends JpaRepository<AttemptDetail, Integer> {
    Optional<AttemptDetail> findByIdAndAttemptId(int id, int attemptId);
}
