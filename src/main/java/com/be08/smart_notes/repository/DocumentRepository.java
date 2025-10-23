package com.be08.smart_notes.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.be08.smart_notes.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
	@Query("UPDATE Document d SET d.title = :title, d.updatedAt = :updatedAt WHERE d.id = :id")
	Document updateTitle(@Param(value = "id") int id, @Param(value = "title") String title,
			@Param(value = "updatedAt") LocalDateTime updatedAt);
}
