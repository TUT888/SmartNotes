package com.be08.smart_notes.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.be08.smart_notes.dto.request.NoteCreationRequest;
import com.be08.smart_notes.enums.DocumentType;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.model.Note;
import com.be08.smart_notes.repository.NoteRepository;

@Service
public class NoteService {
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private DocumentService documentService;
	
	public Note getNote(int id) {
		Note note = noteRepository.findById(id).orElse(null);
		return note;
	}

	@Transactional(rollbackFor = Exception.class)
	public Note createNote(NoteCreationRequest noteCreationRequest) {
		Document newDocument = Document.builder()
				.userId(noteCreationRequest.getUserId())
				.title(noteCreationRequest.getTitle())
				.type(DocumentType.NOTE)
				.createdAt(LocalDateTime.now())
				.build();
		documentService.createDocument(newDocument);
		
		Note newNote = Note.builder()
				.document(newDocument)
				.content(noteCreationRequest.getContent())
				.build();
		noteRepository.save(newNote);
		return newNote;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void deleteNote(int id) {
		documentService.deleteDocument(id);
		
		noteRepository.deleteById(id);
	}
}
