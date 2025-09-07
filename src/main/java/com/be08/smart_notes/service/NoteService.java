package com.be08.smart_notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.entity.NoteEntity;
import com.be08.smart_notes.repository.NoteRepository;

@Service
public class NoteService {
	@Autowired
	private NoteRepository noteRepository;
	
	public NoteEntity getNote(long id) {
		NoteEntity note = noteRepository.findById(id).orElse(null);;
		return note;
	}
	
	public NoteEntity createNewNote(NoteEntity newNote) {
		NoteEntity note = noteRepository.save(newNote);
		return note;
	}
}
