package com.be08.smart_notes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.repository.DocumentRepository;

@Service
public class DocumentService {
	@Autowired
	private DocumentRepository documentRepository;
	
	public List<Document> getAllDocuments() {
		List<Document> documentList = documentRepository.findAll();
		return documentList;
	}
	
	public Document createDocument(Document newDocument) {
		Document createdDocument = documentRepository.save(newDocument);
		return createdDocument;
	}
	
	public Document updateDocumentTitle(int id, String title) {
		Document updatedDocument = documentRepository.updateTitle(id, title, LocalDateTime.now());
		return updatedDocument;
	}
	
	public void deleteDocument(int id) {
		documentRepository.deleteById(id);
	}
}
