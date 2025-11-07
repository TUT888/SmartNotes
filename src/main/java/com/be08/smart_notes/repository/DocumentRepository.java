package com.be08.smart_notes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.be08.smart_notes.model.Document;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findAllByUserId(Integer userId);

    Optional<Document> findByIdAndUserId(int documentId, int userId);
    Optional<Document> findByTitleAndUserId(String title, int userId);

    List<Document> findAllByIdIn(List<Integer> ids);
	List<Document> findAllByUserIdAndIdIn(Integer userId, List<Integer> ids);
}
