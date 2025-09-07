package com.be08.smart_notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.be08.smart_notes.entity.DocumentEntity;
import com.be08.smart_notes.service.DocumentService;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
	@Autowired
	private DocumentService documentService;

	@GetMapping("/all")
	public ResponseEntity<Object> getAllDocuments() {
		List<DocumentEntity> documentList = documentService.getAllDocuments();
		return ResponseEntity.status(HttpStatus.OK).body(documentList);
	}
}
