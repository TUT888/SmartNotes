package com.be08.smart_notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.model.Note;
import com.be08.smart_notes.repository.NoteRepository;

@Service
public class NoteService {
	@Autowired
	private NoteRepository noteRepository;
	
	public Note getNote(int id) {
		Note note = noteRepository.findById(id).orElse(null);
		return note;
	}
	
	public Note createOrUpdateNote(Note newNote) {
		Note createdNote = noteRepository.save(newNote);
		return createdNote;
	}
}
