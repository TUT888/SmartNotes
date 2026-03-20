package com.be08.smart_notes.controller;

import com.be08.smart_notes.common.DefaultConstants;
import com.be08.smart_notes.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Documents", description = "Operation for documents (notes and PDFs)")
@SecurityRequirement(name = "Bearer Authentication")
public class DocumentController {
	DocumentService documentService;

	@GetMapping
    @Operation(summary = "Get all documents by page")
	public ApiResponse<PageResponse<Document>> getAllDocuments(
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = DefaultConstants.PAGE_SIZE) int size) {
        PageResponse<Document> documentList = documentService.getAllDocuments(page, size);
		return ApiResponse.<PageResponse<Document>>builder()
				.message("All document fetched successfully")
				.data(documentList)
				.build();
	}

	@DeleteMapping("/{id}")
    @Operation(summary = "Delete document")
	public ApiResponse<Void> deleteDocument(@PathVariable int id) {
		documentService.deleteDocument(id);
		return ApiResponse.<Void>builder()
				.message("Document deleted successfully")
				.build();
	}
}
