package com.be08.smart_notes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.be08.smart_notes.model.Document;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findAllByUserId(Integer userId);
}
