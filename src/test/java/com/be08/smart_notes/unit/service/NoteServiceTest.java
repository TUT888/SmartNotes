package com.be08.smart_notes.unit.service;

import com.be08.smart_notes.helper.DocumentDataBuilder;
import com.be08.smart_notes.dto.request.NoteUpsertRequest;
import com.be08.smart_notes.dto.response.NoteResponse;
import com.be08.smart_notes.enums.DocumentType;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.helper.UserDataBuilder;
import com.be08.smart_notes.mapper.DocumentMapper;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.repository.DocumentRepository;
import com.be08.smart_notes.service.AuthorizationService;
import com.be08.smart_notes.service.NoteService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
@DisplayName("Note Service Test")
public class NoteServiceTest {
    @Mock
    DocumentRepository documentRepository;
    @Mock
    AuthorizationService authorizationService;
    @Mock
    DocumentMapper documentMapper;

    @InjectMocks
    NoteService noteService;

    User existingUser;
    Document existingNote;
    Document anotherExistingNote;
    NoteResponse existingNoteResponse;
    NoteResponse anotherExistingNoteResponse;

    @BeforeEach
    void setUp() {
        int userId = 100;
        existingUser = UserDataBuilder.createMockUser(userId).build();
        existingNote = DocumentDataBuilder.createMockNote(userId).id(1).userId(100).build();
        anotherExistingNote = DocumentDataBuilder.createMockNote(userId).id(2).userId(100).build();
        existingNoteResponse = DocumentDataBuilder.createMockNoteResponse(existingNote).build();
        anotherExistingNoteResponse = DocumentDataBuilder.createMockNoteResponse(anotherExistingNote).build();

        when(authorizationService.getCurrentUserId()).thenReturn(existingUser.getId());
    }

    // --- Get note --- //
    @Nested
    @DisplayName("getNote(): NoteResponse")
    class GetNoteTest {
        @Test
        void shouldThrowExceptionWhenGivenNonExistentId() {
            // Arrange
            int nonExistentId = 999;
            int userId = existingUser.getId();
            when(documentRepository.findByIdAndUserId(nonExistentId, userId)).thenReturn(Optional.empty());

            // Act & Assert
            AppException actualException = assertThrows(AppException.class, () -> {
                noteService.getNote(nonExistentId);
            });
            assertEquals(ErrorCode.DOCUMENT_NOT_FOUND, actualException.getErrorCode());

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findByIdAndUserId(nonExistentId, userId);
            verify(documentMapper, never()).toNoteResponse(any());
        }

        @Test
        void shouldReturnNoteResponseWhenNoteExists() {
            // Arrange
            int noteId = existingNote.getId();
            int userId = existingUser.getId();
            when(documentRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(existingNote));
            when(documentMapper.toNoteResponse(existingNote)).thenReturn(existingNoteResponse);

            // Act
            NoteResponse actualResponse = noteService.getNote(noteId);

            // Assert
            assertNotNull(actualResponse);
            assertEquals(existingNoteResponse, actualResponse);

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findByIdAndUserId(noteId, userId);
            verify(documentMapper).toNoteResponse(existingNote);
        }
    }

    // -- Get all notes -- //
    @Nested
    @DisplayName("getAllNotes(): List<NoteResponse>")
    class GetAllNotesTest {
        @Test
        void shouldReturnEmptyNoteResponseListWhenListIsEmpty() {
            // Arrange
            int userId = existingUser.getId();
            List<Document> emptyList = Collections.emptyList();
            List<NoteResponse> expectedNoteResponseList = Collections.emptyList();
            when(documentRepository.findAllByUserId(userId)).thenReturn(emptyList);
            when(documentMapper.toNoteResponseList(emptyList)).thenReturn(expectedNoteResponseList);

            // Act
            List<NoteResponse> actualResponse = noteService.getAllNotes();

            // Assert
            assertNotNull(actualResponse);
            assertTrue(actualResponse.isEmpty());

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findAllByUserId(userId);
            verify(documentMapper).toNoteResponseList(emptyList);
        }

        @Test
        void shouldReturnNoteResponseListWhenListIsNotEmpty() {
            // Arrange
            int userId = existingUser.getId();
            List<Document> noteList = List.of(existingNote, anotherExistingNote);
            List<NoteResponse> expectedNoteResponseList = List.of(existingNoteResponse, anotherExistingNoteResponse);
            when(documentRepository.findAllByUserId(userId)).thenReturn(noteList);
            when(documentMapper.toNoteResponseList(noteList)).thenReturn(expectedNoteResponseList);

            // Act
            List<NoteResponse> actualResponse = noteService.getAllNotes();

            // Assert
            assertNotNull(actualResponse);
            assertFalse(actualResponse.isEmpty());
            assertEquals(expectedNoteResponseList.size(), actualResponse.size());
            assertEquals(expectedNoteResponseList, actualResponse);

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findAllByUserId(userId);
            verify(documentMapper).toNoteResponseList(noteList);
        }

        @Test
        void shouldReturnSingleNoteResponseListWhenGivenSingleNote() {
            // Arrange
            int userId = existingUser.getId();
            List<Document> noteList = List.of(existingNote);
            List<NoteResponse> expectedNoteResponseList = List.of(existingNoteResponse);
            when(documentRepository.findAllByUserId(userId)).thenReturn(noteList);
            when(documentMapper.toNoteResponseList(noteList)).thenReturn(expectedNoteResponseList);

            // Act
            List<NoteResponse> actualResponse = noteService.getAllNotes();

            // Assert
            assertNotNull(actualResponse);
            assertEquals(1, actualResponse.size());
            assertEquals(existingNoteResponse, actualResponse.get(0));

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findAllByUserId(userId);
            verify(documentMapper).toNoteResponseList(noteList);
        }
    }

    // --- Create note --- //
    @Nested
    @DisplayName("createNote(): NoteResponse")
    class CreateNoteTest {
        @Test
        void shouldCreateSuccessfullyWhenGivenValidInput() {
            // Arrange
            int newId = 2;
            String newTitle = "Test New Note";
            String newContent = "Test New Content";
            NoteUpsertRequest newRequest = NoteUpsertRequest.builder()
                    .title(newTitle).content(newContent).build();
            Document savedNote = Document.builder().id(newId).userId(existingUser.getId())
                    .title(newTitle).content(newContent).type(DocumentType.NOTE).build();
            NoteResponse expectedResponse = NoteResponse.builder().id(newId)
                    .title(newTitle).content(newContent).build();

            when(documentRepository.save(any(Document.class))).thenReturn(savedNote);
            when(documentMapper.toNoteResponse(savedNote)).thenReturn(expectedResponse);

            // Act
            NoteResponse actualResponse = noteService.createNote(newRequest);

            // Assert
            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).save(any(Document.class));
            verify(documentMapper).toNoteResponse(savedNote);
        }
    }

    // --- Update note --- //
    @Nested
    @DisplayName("updateNote(): NoteResponse")
    class UpdateNoteTest {
        @Test
        void shouldUpdateSuccessfullyWhenNoteExists() {
            // Arrange
            int noteId = existingNote.getId();
            int userId = existingUser.getId();
            String updatedTitle = "Test Updated Title";
            String updatedContent = "Test Updated Content";
            NoteUpsertRequest updateRequest = NoteUpsertRequest.builder()
                    .title(updatedTitle).content(updatedContent).build();
            Document updatedNote = Document.builder().id(noteId).userId(userId)
                    .title(updatedTitle).content(updatedContent).type(DocumentType.NOTE).build();
            NoteResponse expectedResponse = NoteResponse.builder().id(noteId)
                    .title(updatedTitle).content(updatedContent).build();

            when(documentRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(existingNote));
            when(documentRepository.save(any(Document.class))).thenReturn(updatedNote);
            when(documentMapper.toNoteResponse(updatedNote)).thenReturn(expectedResponse);

            // Act
            NoteResponse actualResponse = noteService.updateNote(noteId, updateRequest);

            // Assert
            assertNotNull(actualResponse);
            assertEquals(expectedResponse, actualResponse);

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findByIdAndUserId(noteId, userId);
            verify(documentRepository).save(any(Document.class));
            verify(documentMapper).toNoteResponse(updatedNote);
        }

        @Test
        void shouldThrowExceptionWhenNoteNotFound() {
            // Arrange
            int noteId = 999;
            int userId = existingUser.getId();
            String updatedTitle = "Test Updated Title";
            String updatedContent = "Test Updated Content";
            NoteUpsertRequest updateRequest = NoteUpsertRequest.builder()
                    .title(updatedTitle).content(updatedContent).build();

            when(documentRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.empty());

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> {
                noteService.updateNote(noteId, updateRequest);
            });
            assertEquals(ErrorCode.DOCUMENT_NOT_FOUND, exception.getErrorCode());

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findByIdAndUserId(noteId, userId);
            verify(documentRepository, never()).save(any());
            verify(documentMapper, never()).toNoteResponse(any());
        }
    }

    // --- Delete note --- //
    @Nested
    @DisplayName("deleteNote(): void")
    class DeleteNoteTest {
        @Test
        void shouldDeleteSuccessfullyWhenNoteExists() {
            // Arrange
            int noteId = existingNote.getId();
            int userId = existingUser.getId();

            when(documentRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.of(existingNote));

            // Act
            noteService.deleteNote(noteId);

            // Assert
            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findByIdAndUserId(noteId, userId);
            verify(documentRepository).deleteById(noteId);
        }

        @Test
        void shouldThrowExceptionWhenNoteNotFound() {
            // Arrange
            int noteId = 999;
            int userId = existingUser.getId();

            when(documentRepository.findByIdAndUserId(noteId, userId)).thenReturn(Optional.empty());

            // Act & Assert
            AppException exception = assertThrows(AppException.class, () -> {
                noteService.deleteNote(noteId);
            });
            assertEquals(ErrorCode.DOCUMENT_NOT_FOUND, exception.getErrorCode());

            verify(authorizationService).getCurrentUserId();
            verify(documentRepository).findByIdAndUserId(noteId, userId);
            verify(documentRepository, never()).deleteById(anyInt());
        }
    }

    // ------ Methods that returns entities ------ //
    @Nested
    @DisplayName("getAllNotesByUserIdAndIds(): List<Document>")
    class GetAllNotesByUserIdAndIdsTest {
        @Test
        void shouldReturnMatchingNotesWhenNotesExist() {
            // Arrange
            int userId = existingUser.getId();
            List<Integer> noteIds = List.of(existingNote.getId(), anotherExistingNote.getId());
            List<Document> expectedNotes = List.of(existingNote, anotherExistingNote);

            when(documentRepository.findAllByUserIdAndIdIn(userId, noteIds)).thenReturn(expectedNotes);

            // Act
            List<Document> actualNotes = noteService.getAllNotesByUserIdAndIds(userId, noteIds);

            // Assert
            assertNotNull(actualNotes);
            assertEquals(2, actualNotes.size());
            assertEquals(expectedNotes, actualNotes);

            verify(documentRepository).findAllByUserIdAndIdIn(userId, noteIds);
        }

        @Test
        void shouldReturnEmptyListWhenNoMatches() {
            // Arrange
            int userId = existingUser.getId();
            List<Integer> noteIds = List.of(998, 999);

            when(documentRepository.findAllByUserIdAndIdIn(userId, noteIds)).thenReturn(Collections.emptyList());

            // Act
            List<Document> actualNotes = noteService.getAllNotesByUserIdAndIds(userId, noteIds);

            // Assert
            assertNotNull(actualNotes);
            assertTrue(actualNotes.isEmpty());

            verify(documentRepository).findAllByUserIdAndIdIn(userId, noteIds);
        }

        @Test
        void shouldReturnEmptyListWhenGivenEmptyIdList() {
            // Arrange
            int userId = existingUser.getId();
            List<Integer> emptyIds = Collections.emptyList();

            when(documentRepository.findAllByUserIdAndIdIn(userId, emptyIds)).thenReturn(Collections.emptyList());

            // Act
            List<Document> actualNotes = noteService.getAllNotesByUserIdAndIds(userId, emptyIds);

            // Assert
            assertNotNull(actualNotes);
            assertTrue(actualNotes.isEmpty());

            verify(documentRepository).findAllByUserIdAndIdIn(userId, emptyIds);
        }

        @Test
        void shouldReturnSingleNoteWhenGivenSingleId() {
            // Arrange
            int userId = existingUser.getId();
            List<Integer> noteIds = List.of(existingNote.getId());
            List<Document> expectedNotes = List.of(existingNote);

            when(documentRepository.findAllByUserIdAndIdIn(userId, noteIds)).thenReturn(expectedNotes);

            // Act
            List<Document> actualNotes = noteService.getAllNotesByUserIdAndIds(userId, noteIds);

            // Assert
            assertNotNull(actualNotes);
            assertEquals(expectedNotes.size(), actualNotes.size());
            assertEquals(existingNote, actualNotes.get(0));

            verify(documentRepository).findAllByUserIdAndIdIn(userId, noteIds);
        }

        @Test
        void shouldReturnOnlyMatchingNotesWhenGivenPartialMatchedInputs() {
            // Arrange
            int userId = existingUser.getId();
            List<Integer> noteIds = List.of(existingNote.getId(), 999);  // Only ID 1 exists
            List<Document> expectedNotes = List.of(existingNote);  // Only returns existing note

            when(documentRepository.findAllByUserIdAndIdIn(userId, noteIds)).thenReturn(expectedNotes);

            // Act
            List<Document> actualNotes = noteService.getAllNotesByUserIdAndIds(userId, noteIds);

            // Assert
            assertNotNull(actualNotes);
            assertEquals(expectedNotes.size(), actualNotes.size());
            assertEquals(existingNote, actualNotes.get(0));

            verify(documentRepository).findAllByUserIdAndIdIn(userId, noteIds);
        }
    }
}
