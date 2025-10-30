package com.be08.smart_notes.service;

import java.util.List;

import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.repository.DocumentRepository;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    private static final String SYSTEM_SOURCE_TITLE = "__SYSTEM_UNFILED_SOURCE__";

    public List<Document> getAllDocuments() {
        List<Document> documentList = documentRepository.findAll();
        return documentList;
    }

    public void deleteDocument(int id) {
        // Get current user ID from claim sub of JWT token (security context)
        int currentUserId = getCurrentUserId();

        // Validate ownership and existence of document
        Document document = getDocumentIfOwned(id, currentUserId);

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

    /**
     * Get the currently authenticated user's ID
     * @return int userId
     */
    private int getCurrentUserId(){
        // Extract user ID from security context
        String userIdString = SecurityContextHolder.getContext().getAuthentication().getName();
        return Integer.parseInt(userIdString);
    }
}