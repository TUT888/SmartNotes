package com.be08.smart_notes.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.dto.request.NoteUpsertRequest;
import com.be08.smart_notes.enums.DocumentType;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.repository.DocumentRepository;

@Service
public class NoteService {
    @Autowired
    private DocumentRepository documentRepository;

    public Document getNote(int noteId) {
        Document note = documentRepository.findById(noteId).orElse(null);
        return note;
    }

    public Document createNote(NoteUpsertRequest newData) {
        Document newNote = Document.builder()
                .userId(newData.getUserId())
                .title(newData.getTitle())
                .content(newData.getContent())
                .type(DocumentType.NOTE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        documentRepository.save(newNote);
        return newNote;
    }

    public void updateNote(int noteId, NoteUpsertRequest updateData) {
        Document note = documentRepository.findById(noteId).orElse(null);

        note.setTitle(updateData.getTitle());
        note.setUpdatedAt(LocalDateTime.now());;
        note.setContent(updateData.getContent());
        documentRepository.save(note);
    }

    public void deleteNote(int noteId) {
        documentRepository.deleteById(noteId);
    }

    public List<Document> getAllNotesByIds(List<Integer> noteIds) {
        return documentRepository.findAllByIdIn(noteIds);
    }
}