package com.be08.smart_notes.helper;

import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.enums.DocumentType;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.model.User;

import java.time.LocalDateTime;

public class DocumentDataBuilder {
    public static User.UserBuilder createUser(int userId) {
        return User.builder().id(userId);
    }

    public static Document.DocumentBuilder createSampleNote(int userId) {
        return Document.builder()
                .userId(userId).type(DocumentType.NOTE)
                .title("Sample Note").content("Sample Note Content")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());
    }

    public static NoteResponse.NoteResponseBuilder createNoteResponse(Document note) {
        return NoteResponse.builder().id(note.getId())
                .title(note.getTitle()).content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt());
    }
}
