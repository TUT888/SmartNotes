package com.be08.smart_notes.controller;

import com.be08.smart_notes.common.DefaultConstants;
import com.be08.smart_notes.dto.filter.BasicFilterDTO;
import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.dto.request.NoteUpsertRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.service.NoteService;

@RestController
@RequestMapping("/api/documents/notes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Notes", description = "All operations for notes")
@SecurityRequirement(name = "Bearer Authentication")
public class NoteController {
	NoteService noteService;

	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new note")
	public ApiResponse<NoteResponse> createNote(@RequestBody @Valid NoteUpsertRequest noteCreationRequest) {
        NoteResponse createdNote = noteService.createNote(noteCreationRequest);
		return ApiResponse.<NoteResponse>builder()
				.message("Note created successfully")
				.data(createdNote)
				.build();
	}

	@GetMapping("/{id}")
    @Operation(summary = "Get note")
	public ApiResponse<NoteResponse> getNote(@PathVariable int id) {
		NoteResponse note = noteService.getNote(id);
		return ApiResponse.<NoteResponse>builder()
				.message("Note fetched successfully")
				.data(note)
				.build();
	}

    @GetMapping
    @Operation(summary = "Get all notes by page")
    public ApiResponse<PageResponse<NoteResponse>> getAllNotes(
            @ModelAttribute BasicFilterDTO filterDTO,
            @RequestParam(required = false, defaultValue = DefaultConstants.SORT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = DefaultConstants.SORT_ORDER) String sortOrder,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_SIZE) int size) {
        PageResponse<NoteResponse> pageResponse = noteService.getAllNotes(filterDTO, sortBy, sortOrder, page, size);
        return ApiResponse.<PageResponse<NoteResponse>>builder()
                .message("Note fetched successfully")
                .data(pageResponse)
                .build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update note")
	public ApiResponse<NoteResponse> updateNote(@PathVariable int id, @RequestBody @Valid NoteUpsertRequest noteUpdateRequest) {
        NoteResponse note = noteService.updateNote(id, noteUpdateRequest);
		return ApiResponse.<NoteResponse>builder()
				.message("Note updated successfully")
                .data(note)
				.build();
	}

	@DeleteMapping("/{id}")
    @Operation(summary = "Delete note")
	public ApiResponse<Void> deleteNote(@PathVariable int id) {
		noteService.deleteNote(id);
		return ApiResponse.<Void>builder()
				.message("Note deleted successfully")
				.build();
	}
}
