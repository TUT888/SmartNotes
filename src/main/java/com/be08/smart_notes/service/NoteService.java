package com.be08.smart_notes.service;

import java.time.LocalDateTime;
import java.util.List;

import com.be08.smart_notes.dto.filter.BasicFilterDTO;
import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.dto.response.PageResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.DocumentMapper;
import com.be08.smart_notes.specification.NoteSpecificationBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
				.build();
		
		Document savedNote = documentRepository.save(newNote);
		return documentMapper.toNoteResponse(savedNote);
	}

    public PageResponse<NoteResponse> getAllNotes(BasicFilterDTO filterDTO, String sortBy, String sortOrder, int pageNumber, int pageSize) {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Filtering and sorting
        Specification<Document> spec = NoteSpecificationBuilder.getSpecification(currentUserId, filterDTO);
        Sort sortOption = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sortOption);

        // Get note
        Page<Document> page = documentRepository.findAll(spec, pageable);
        List<NoteResponse> noteResponses = page.getContent().stream().map(documentMapper::toNoteResponse).toList();

        return PageResponse.<NoteResponse>builder()
                .pageInfo(PageResponse.PageInfo.builder()
                        .currentPage(pageNumber)
                        .pageSize(pageSize)
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getTotalElements()).build())
                .pageData(noteResponses).build();
    }

    public PageResponse<NoteResponse> getAllNotes(int pageNumber, int pageSize) {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Get page data
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Document> page = documentRepository.findAllByUserId(currentUserId, pageable);

        // Get note
        List<NoteResponse> noteResponses = page.getContent().stream().map(documentMapper::toNoteResponse).toList();

        return PageResponse.<NoteResponse>builder()
                .pageInfo(PageResponse.PageInfo.builder()
                        .currentPage(pageNumber)
                        .pageSize(pageSize)
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getTotalElements()).build())
                .pageData(noteResponses).build();
    }

    public List<NoteResponse> getAllNotes() {
        // Get current user id
        int currentUserId = authorizationService.getCurrentUserId();

        // Get note
        List<Document> noteList = documentRepository.findAllByUserId(currentUserId);

        return documentMapper.toNoteResponseList(noteList);
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
        return documentRepository.findAllByUserIdAndIdIn(userId, noteIds);
    }

    public List<Document> getAllNotesByIds(List<Integer> noteIds) {
        return documentRepository.findAllByIdIn(noteIds);
    }
}
