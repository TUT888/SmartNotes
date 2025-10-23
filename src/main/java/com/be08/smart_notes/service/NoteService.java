package com.be08.smart_notes.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.dto.request.NoteCreationRequest;
import com.be08.smart_notes.dto.request.NoteUpdateRequest;
import com.be08.smart_notes.enums.DocumentType;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.model.Note;
import com.be08.smart_notes.repository.DocumentRepository;
import com.be08.smart_notes.repository.NoteRepository;

@Service
public class NoteService {
	@Autowired
	private NoteRepository noteRepository;
	@Autowired
	private DocumentRepository documentRepository;

	public Note getNote(int noteId) {
		Note note = noteRepository.findById(noteId).orElse(null);
		return note;
	}

	public Note createNote(NoteCreationRequest noteCreationRequest) {
		Document newDocument = Document.builder()
				.userId(noteCreationRequest.getUserId())
				.title(noteCreationRequest.getTitle())
				.type(DocumentType.NOTE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		
		Note newNote = Note.builder()
				.document(newDocument)
				.content(noteCreationRequest.getContent())
				.build();
		noteRepository.save(newNote);
		return newNote;
	}

	public void updateNote(int id, NoteUpdateRequest updateData) {
		Note note = noteRepository.findById(id).orElse(null);
		
		note.getDocument().setTitle(updateData.getTitle());
		note.getDocument().setUpdatedAt(LocalDateTime.now());;
		note.setContent(updateData.getContent());
		noteRepository.save(note);
	}
	
	public void deleteNote(int noteId) {
		documentRepository.deleteById(noteId);
	}
}
