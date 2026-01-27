package com.be08.smart_notes.controller;

import com.be08.smart_notes.common.DefaultConstants;
import com.be08.smart_notes.dto.filter.BasicFilterDTO;
import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.dto.response.PageResponse;
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
