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
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Get note
		Document note = documentRepository.findByIdAndUserId(noteId, currentUserId).orElseThrow(() -> {
            log.error("Note with id {} not found in user's account", noteId);
            return new AppException(ErrorCode.DOCUMENT_NOT_FOUND);
        });

		return documentMapper.toNoteResponse(note);
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
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Get note
        Document note = documentRepository.findByIdAndUserId(noteId, currentUserId).orElseThrow(() -> {
            log.error("Note with id {} not found in user's account", noteId);
            return new AppException(ErrorCode.DOCUMENT_NOT_FOUND);
        });
		
		note.setTitle(updateData.getTitle());
		note.setUpdatedAt(LocalDateTime.now());;
		note.setContent(updateData.getContent());
        Document updatedNote = documentRepository.save(note);
        return documentMapper.toNoteResponse(updatedNote);
	}
	
	public void deleteNote(int noteId) {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Get document
        Document note = documentRepository.findByIdAndUserId(noteId, currentUserId).orElseThrow(() -> {
            log.error("Note with id {} not found in user's account", noteId);
            return new AppException(ErrorCode.DOCUMENT_NOT_FOUND);
        });

		documentRepository.deleteById(noteId);
	}

    // ------ Methods that returns entities ------ //
    public List<Document> getAllNotesByUserIdAndIds(int userId, List<Integer> noteIds) {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        return documentRepository.findAllByUserIdAndIdIn(currentUserId, noteIds);
    }

    public List<Document> getAllNotesByIds(List<Integer> noteIds) {
        return documentRepository.findAllByIdIn(noteIds);
    }
}
