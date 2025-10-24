package com.be08.smart_notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.service.DocumentService;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
	@Autowired
	private DocumentService documentService;

	@GetMapping
	public ResponseEntity<Object> getAllDocuments() {
		List<Document> documentList = documentService.getAllDocuments();
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
