package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.response.PageResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocumentController {
	DocumentService documentService;

	@GetMapping
	public ResponseEntity<Object> getAllDocuments(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "6") int size) {
        PageResponse<Document> documentList = documentService.getAllDocuments(page, size);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("All document fetched successfully")
				.data(documentList)
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteDocument(@PathVariable int id) {
		documentService.deleteDocument(id);
		ApiResponse<Object> apiResponse = ApiResponse.builder()
				.message("Document deleted successfully")
				.build();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
}
