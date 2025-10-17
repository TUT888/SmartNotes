package com.be08.smart_notes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.model.Note;
import com.be08.smart_notes.repository.DocumentRepository;

@Service
public class DocumentService {
	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private NoteService noteService;
	
	public List<Document> getAllDocuments() {
		List<Document> documentList = documentRepository.findAll();
		return documentList;
	}
	
	public Document createDocument(Document newDocument) {
		Document createdDocument = documentRepository.save(newDocument);
		
		Note newNote = Note.builder()
				.id(createdDocument.getId())
				.content("")
				.build();
		noteService.createOrUpdateNote(newNote);
		
		return createdDocument;
	}
}
