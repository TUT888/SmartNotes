package com.be08.smart_notes.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.DocumentMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.be08.smart_notes.dto.request.NoteUpsertRequest;
import com.be08.smart_notes.enums.DocumentType;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.repository.DocumentRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NoteService {
    AuthorizationService authorizationService;
	DocumentRepository documentRepository;
    DocumentMapper documentMapper;

	public NoteResponse getNote(int noteId) {
        // Get note
		Document note = documentRepository.findById(noteId).orElseThrow(() -> {
            log.error("Note with id {} not found", noteId);
            return new AppException(ErrorCode.DOCUMENT_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(note.getUserId());

		return documentMapper.toNoteResponse(note);
	}

    public List<Document> getNotesByIds(List<Integer> noteIds) {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        return documentRepository.findAllByUserIdAndIdIn(currentUserId, noteIds);
    }

	public NoteResponse createNote(NoteUpsertRequest newData) {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Create new note
		Document newNote = Document.builder()
				.userId(currentUserId)
				.title(newData.getTitle())
				.content(newData.getContent())
				.type(DocumentType.NOTE)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();
		
		documentRepository.save(newNote);
		return documentMapper.toNoteResponse(newNote);
	}

	public NoteResponse updateNote(int noteId, NoteUpsertRequest updateData) {
        // Get note
		Document note = documentRepository.findById(noteId).orElseThrow(() -> {
            log.error("Note with id {} not found", noteId);
            return new AppException(ErrorCode.DOCUMENT_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(note.getUserId());
		
		note.setTitle(updateData.getTitle());
		note.setUpdatedAt(LocalDateTime.now());;
		note.setContent(updateData.getContent());
        Document updatedNote = documentRepository.save(note);
        return documentMapper.toNoteResponse(updatedNote);
	}
	
	public void deleteNote(int noteId) {
        // Get document
        Document note = documentRepository.findById(noteId).orElseThrow(() -> {
            log.error("Note with id {} not found", noteId);
            throw new AppException(ErrorCode.DOCUMENT_NOT_FOUND);
        });

        // Check ownership
        authorizationService.validateOwnership(note.getUserId());

		documentRepository.deleteById(noteId);
	}
}
