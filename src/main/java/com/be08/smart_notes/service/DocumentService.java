package com.be08.smart_notes.service;

import java.util.List;

import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.repository.DocumentRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DocumentService {
    AuthorizationService authorizationService;
	DocumentRepository documentRepository;

	public List<Document> getAllDocuments() {
        // Get current user
        int currentUserId = authorizationService.getCurrentUserId();

        // Get document
        return documentRepository.findAllByUserId(currentUserId);
	}

	public void deleteDocument(int id) {
        // Get document
        Document document = documentRepository.findById(id).orElseThrow(() -> {
            log.error("Document with id {} not found", id);
            throw new AppException(ErrorCode.DOCUMENT_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(document.getUserId());

		documentRepository.delete(document);
	}
}
