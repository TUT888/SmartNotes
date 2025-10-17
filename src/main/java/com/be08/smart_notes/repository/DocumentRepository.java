package com.be08.smart_notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.be08.smart_notes.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer>{
	
}
