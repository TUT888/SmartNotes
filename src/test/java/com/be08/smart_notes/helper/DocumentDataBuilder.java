package com.be08.smart_notes.helper;

import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.enums.DocumentType;
import com.be08.smart_notes.model.Document;

import java.time.LocalDateTime;

public class DocumentDataBuilder {
    /**
     * Create a sample mock document (note) with given information.
     * This method should not be used with real database interaction
     * @param userId mock user ID
     * @return Document.DocumentBuilder
     */
    public static Document.DocumentBuilder createMockNote(int userId) {
        return Document.builder()
                .userId(userId).type(DocumentType.NOTE)
                .title("Sample Note").content("Sample Note Content")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());
    }

    /**
     * Create a sample mock user object with given information.
     * This method should not be used with real database interaction
     * @param note a note object to be parsed to note response
     * @return NoteResponse.NoteResponseBuilder
     */
    public static NoteResponse.NoteResponseBuilder createMockNoteResponse(Document note) {
        return NoteResponse.builder().id(note.getId())
                .title(note.getTitle()).content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt());
    }
}
