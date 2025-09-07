package com.be08.smart_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.entity.DocumentEntity;
import com.be08.smart_notes.repository.DocumentRepository;

@Service
public class DocumentService {
	@Autowired
	private DocumentRepository documentRepository;
	
	public List<DocumentEntity> getAllDocuments() {
		List<DocumentEntity> documentList = documentRepository.findAll();
		return documentList;
	}
}
