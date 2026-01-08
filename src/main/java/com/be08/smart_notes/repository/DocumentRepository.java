package com.be08.smart_notes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.be08.smart_notes.model.Document;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findAllByUserId(int userId);
    Page<Document> findAllByUserId(int userId, Pageable pageable);

    Optional<Document> findByIdAndUserId(int documentId, int userId);
    Optional<Document> findFirstByTitleAndUserId(String title, int userId);

    List<Document> findAllByIdIn(List<Integer> ids);
	List<Document> findAllByUserIdAndIdIn(int userId, List<Integer> ids);
}
