package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.response.NoteResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.dto.request.NoteUpsertRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.service.NoteService;

import java.util.List;

@RestController
@RequestMapping("/api/documents/notes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NoteController {
	NoteService noteService;

	@PostMapping
	public ResponseEntity<Object> createNote(@RequestBody @Valid NoteUpsertRequest noteCreationRequest) {
        NoteResponse createdNote = noteService.createNote(noteCreationRequest);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("Note created successfully")
				.data(createdNote)
				.build();
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getNote(@PathVariable int id) {
		NoteResponse note = noteService.getNote(id);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("Note fetched successfully")
				.data(note)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

    @GetMapping
    public ResponseEntity<Object> getAllNotes() {
        List<NoteResponse> noteList = noteService.getAllNotes();
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Notes fetched successfully")
                .data(noteList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{id}")
	public ResponseEntity<Object> updateNote(@PathVariable int id, @RequestBody @Valid NoteUpsertRequest noteUpdateRequest) {
        NoteResponse note = noteService.updateNote(id, noteUpdateRequest);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("Note updated successfully")
                .data(note)
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
