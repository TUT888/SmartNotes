package com.be08.smart_notes.controller;

import com.be08.smart_notes.common.DefaultConstants;
import com.be08.smart_notes.dto.filter.BasicFilterDTO;
import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/api/documents/notes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Notes", description = "All operations for notes")
public class NoteController {
	NoteService noteService;

	@PostMapping
    @Operation(summary = "Create new note")
	public ResponseEntity<Object> createNote(@RequestBody @Valid NoteUpsertRequest noteCreationRequest) {
        NoteResponse createdNote = noteService.createNote(noteCreationRequest);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("Note created successfully")
				.data(createdNote)
				.build();
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
	}

	@GetMapping("/{id}")
    @Operation(summary = "Get note")
	public ResponseEntity<Object> getNote(@PathVariable int id) {
		NoteResponse note = noteService.getNote(id);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("Note fetched successfully")
				.data(note)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

    @GetMapping
    @Operation(summary = "Get all notes by page")
    public ResponseEntity<Object> getAllNotes(
            @ModelAttribute BasicFilterDTO filterDTO,
            @RequestParam(required = false, defaultValue = DefaultConstants.SORT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = DefaultConstants.SORT_ORDER) String sortOrder,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_SIZE) int size) {
        PageResponse<NoteResponse> pageResponse = noteService.getAllNotes(filterDTO, sortBy, sortOrder, page, size);
        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .message("Note fetched successfully")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update note")
	public ResponseEntity<Object> updateNote(@PathVariable int id, @RequestBody @Valid NoteUpsertRequest noteUpdateRequest) {
        NoteResponse note = noteService.updateNote(id, noteUpdateRequest);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("Note updated successfully")
                .data(note)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@DeleteMapping("/{id}")
    @Operation(summary = "Delete note")
	public ResponseEntity<Object> deleteNote(@PathVariable int id) {
		noteService.deleteNote(id);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("Note deleted successfully")
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
}
