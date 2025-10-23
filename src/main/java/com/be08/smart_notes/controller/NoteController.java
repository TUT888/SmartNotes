package com.be08.smart_notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.be08.smart_notes.dto.request.NoteCreationRequest;
import com.be08.smart_notes.dto.request.NoteUpdateRequest;
import com.be08.smart_notes.model.Note;
import com.be08.smart_notes.service.NoteService;

@RestController
@RequestMapping("/api/document/note")
public class NoteController {
	@Autowired
	private NoteService noteService;

	@PostMapping
	public ResponseEntity<Object> createNote(@RequestBody NoteCreationRequest noteCreationRequest) {
		Note createdNote = noteService.createNote(noteCreationRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getNote(@PathVariable int id) {
		Note note = noteService.getNote(id);
		return ResponseEntity.status(HttpStatus.OK).body(note);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> updateNote(@PathVariable int id, @RequestBody NoteUpdateRequest noteUpdateRequest) {
		noteService.updateNote(id, noteUpdateRequest);
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteNote(@PathVariable int id) {
		noteService.deleteNote(id);
		return ResponseEntity.status(HttpStatus.OK).body("OK");
	}
}
