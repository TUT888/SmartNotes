package com.be08.smart_notes.service;

import java.util.List;

import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.repository.DocumentRepository;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> getAllDocuments() {
        List<Document> documentList = documentRepository.findAll();
        return documentList;
    }

    public void deleteDocument(int id) {
        documentRepository.deleteById(id);
    }
}