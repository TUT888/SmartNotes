package com.be08.smart_notes.service;

import java.util.List;

import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private static final String SYSTEM_SOURCE_TITLE = "__SYSTEM_UNFILED_SOURCE__";

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

        // Prevent deletion of system source document
        if(SYSTEM_SOURCE_TITLE.equals(document.getTitle())){
            throw new AppException(ErrorCode.DOCUMENT_CANNOT_BE_DELETED);
        }

        documentRepository.deleteById(id);
    }

    /**
     * Get document if owned by user
     * @param documentId
     * @param userId
     * @return Document
     * @throws AppException if document not found or not owned by user
     */
    public Document getDocumentIfOwned(int documentId, int userId){
        return documentRepository.findByIdAndUserId(documentId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.DOCUMENT_NOT_FOUND));
    }

    public Document getSystemSourceDocument(int userId){
        return documentRepository.findByTitleAndUserId(SYSTEM_SOURCE_TITLE, userId)
                .orElseThrow(() -> new AppException(ErrorCode.SYSTEM_SOURCE_DOCUMENT_NOT_FOUND));
    }
}