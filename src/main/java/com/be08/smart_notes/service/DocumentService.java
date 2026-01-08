package com.be08.smart_notes.service;

import java.util.List;

import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

	public PageResponse<Document> getAllDocuments(int pageNumber, int pageSize) {
        // Get current user
        int currentUserId = authorizationService.getCurrentUserId();

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Document> page = documentRepository.findAllByUserId(currentUserId, pageable);

        List<Document> documents = page.stream().toList();

        return PageResponse.<Document>builder()
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageData(documents).build();
	}

	public void deleteDocument(int id) {
        // Get current user
        int currentUserId = authorizationService.getCurrentUserId();

        // Get document
        Document document = documentRepository.findByIdAndUserId(id, currentUserId).orElseThrow(() -> {
            log.error("Document with id {} not found in user's account", id);
            throw new AppException(ErrorCode.DOCUMENT_NOT_FOUND);
        });

        // Prevent deletion of system source document
        if(SYSTEM_SOURCE_TITLE.equals(document.getTitle())){
            throw new AppException(ErrorCode.DOCUMENT_CANNOT_BE_DELETED);
        }

		documentRepository.delete(document);
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

    /**
     * Get system source document for user
     * @param userId
     * @return Document
     * @throws AppException if system source document not found
     */
    public Document getSystemSourceDocument(int userId){
        return documentRepository.findFirstByTitleAndUserId(SYSTEM_SOURCE_TITLE, userId)
                .orElseThrow(() -> new AppException(ErrorCode.SYSTEM_SOURCE_DOCUMENT_NOT_FOUND));
    }
}
