package com.be08.smart_notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.be08.smart_notes.entity.NoteEntity;
import com.be08.smart_notes.service.NoteService;

@RestController
@RequestMapping("/api/note")
public class NoteController {
	@Autowired
	private NoteService noteService;

	@GetMapping("/{id}")
	public ResponseEntity<Object> getNote(@PathVariable Long id) {
		NoteEntity note = noteService.getNote(id);
		return ResponseEntity.status(HttpStatus.OK).body(note);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Object> createNote(@RequestBody NoteEntity note) {
		NoteEntity createdNote = noteService.createNewNote(note);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
	}
}
