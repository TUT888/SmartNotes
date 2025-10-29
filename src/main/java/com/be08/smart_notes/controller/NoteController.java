package com.be08.smart_notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.be08.smart_notes.dto.request.NoteUpsertRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.service.NoteService;
import com.be08.smart_notes.validation.group.OnCreate;
import com.be08.smart_notes.validation.group.OnUpdate;

@RestController
@RequestMapping("/api/document/note")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @PostMapping
    public ResponseEntity<Object> createNote(@Validated(OnCreate.class) @RequestBody NoteUpsertRequest noteCreationRequest) {
        Document createdNote = noteService.createNote(noteCreationRequest);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Note created successfully")
                .data(createdNote)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getNote(@PathVariable int id) {
        Document note = noteService.getNote(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Note fetched successfully")
                .data(note)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateNote(@PathVariable int id, @Validated(OnUpdate.class) @RequestBody NoteUpsertRequest noteUpdateRequest) {
        noteService.updateNote(id, noteUpdateRequest);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Note updated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteNote(@PathVariable int id) {
        noteService.deleteNote(id);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Note deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}