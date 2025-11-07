package com.be08.smart_notes.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.be08.smart_notes.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findAllByUserId(Integer userId);

    Optional<Document> findByIdAndUserId(int documentId, int userId);

    Optional<Document> findByTitleAndUserId(String title, int userId);
}