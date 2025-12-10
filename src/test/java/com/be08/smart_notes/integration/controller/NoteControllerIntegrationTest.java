package com.be08.smart_notes.integration.controller;

import com.be08.smart_notes.dto.request.NoteUpsertRequest;
import com.be08.smart_notes.helper.DocumentDataBuilder;
import com.be08.smart_notes.helper.UserDataBuilder;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.repository.DocumentRepository;
import com.be08.smart_notes.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;

import static com.be08.smart_notes.helper.JwtBuilder.jwtWithUserId;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
@DisplayName("Note Controller Integration Test")
public class NoteControllerIntegrationTest extends BaseIntegration {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    UserRepository userRepository;

    @MockitoBean
    JwtEncoder jwtEncoder;
    @MockitoBean
    JWKSource<SecurityContext> jwkSource;
    @MockitoBean
    KeyPair signingKeyPair;

    final String BASE_URI = "/api/documents/notes";
    int TEST_USER_ID;
    int OTHER_USER_ID;
    Document existingNote;
    Document anotherNote;

    @BeforeEach
    void setUpEach() {
        userRepository.deleteAll();
        documentRepository.deleteAll();

        User testUser1 = userRepository.save(UserDataBuilder.createUser("example-user@gmail.com").build());
        User testUser2 = userRepository.save(UserDataBuilder.createUser("another-user@gmail.com").build());

        TEST_USER_ID = testUser1.getId();
        OTHER_USER_ID = testUser2.getId();

        existingNote = documentRepository.save(DocumentDataBuilder.createMockNote(TEST_USER_ID).build());
        anotherNote = documentRepository.save(DocumentDataBuilder.createMockNote(OTHER_USER_ID).build());
    }

    @Nested
    @DisplayName("POST /api/documents/notes")
    class CreateNoteIntegrationTests {
        @Test
        void shouldCreateNoteAndPersist() throws Exception {
            // Arrange
            String title = "Integration Test Note";
            String content = "Integration Test Content";
            NoteUpsertRequest request = NoteUpsertRequest.builder()
                    .title(title).content(content).build();

            int initialSize = documentRepository.findAllByUserId(TEST_USER_ID).size();

            // Act & Assert
            MvcResult result = mockMvc.perform(
                        post(BASE_URI)
                                .with(jwtWithUserId(TEST_USER_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.title").value(title))
                    .andExpect(jsonPath("$.data.content").value(content))
                    .andExpect(jsonPath("$.data.id").exists())
                    .andExpect(jsonPath("$.data.createdAt").exists())
                    .andReturn();

            int createdNoteId = objectMapper.readTree(result.getResponse().getContentAsString()).get("data").get("id").asInt();
            Document createdNote = documentRepository.findById(createdNoteId).orElseThrow();

            assertEquals(TEST_USER_ID, createdNote.getUserId());
            assertEquals(initialSize + 1, documentRepository.findAllByUserId(TEST_USER_ID).size());
        }

        @Test
        void shouldRejectRequestWithNullTitle() throws Exception {
            // Arrange
            NoteUpsertRequest invalidRequest = NoteUpsertRequest.builder()
                    .title(null).content("Content").build();
            int initialSize = documentRepository.findAllByUserId(TEST_USER_ID).size();

            // Act & Assert
            mockMvc.perform(
                        post(BASE_URI)
                                .with(jwtWithUserId(TEST_USER_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest))
                    )
                    .andExpect(status().isBadRequest());

            assertEquals(initialSize, documentRepository.findAllByUserId(TEST_USER_ID).size());
        }

        @Test
        void shouldRejectRequestWithNullContent() throws Exception {
            // Arrange
            NoteUpsertRequest invalidRequest = NoteUpsertRequest.builder()
                    .title("Title").content(null).build();
            int initialSize = documentRepository.findAllByUserId(TEST_USER_ID).size();

            // Act & Assert
            mockMvc.perform(
                        post(BASE_URI)
                                .with(jwtWithUserId(TEST_USER_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest))
                    )
                    .andExpect(status().isBadRequest());

            assertEquals(initialSize, documentRepository.findAllByUserId(TEST_USER_ID).size());
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            // Arrange
            NoteUpsertRequest request = NoteUpsertRequest.builder()
                    .title("Test Note").content("Content").build();

            // Act & Assert
            mockMvc.perform(
                        post(BASE_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("GET /api/documents/notes")
    class GetAllNotesIntegrationTests {
        @Test
        void shouldRetrieveAllNotesForCurrentUser() throws Exception {
            // Act & Assert
            mockMvc.perform(
                        get(BASE_URI)
                                .with(jwtWithUserId(TEST_USER_ID))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(1)));
        }

        @Test
        void shouldReturnEmptyListWhenUserHasNoNotes() throws Exception {
            // Arrange
            documentRepository.deleteAll();

            // Act & Assert
            mockMvc.perform(
                        get(BASE_URI)
                                .with(jwtWithUserId(TEST_USER_ID))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(0)));
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            // Act & Assert
            mockMvc.perform(get(BASE_URI))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("PATCH /api/documents/notes/{id}")
    class UpdateNoteIntegrationTests {
        @Test
        void shouldUpdateExistingNote() throws Exception {
            // Arrange
            String updatedTitle = "Updated Title";
            String updatedContent = "Updated Content";
            NoteUpsertRequest updateRequest = NoteUpsertRequest.builder()
                    .title(updatedTitle).content(updatedContent).build();

            // Act & Assert
            mockMvc.perform(
                        patch(BASE_URI + "/" + existingNote.getId())
                                .with(jwtWithUserId(TEST_USER_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(existingNote.getId()))
                    .andExpect(jsonPath("$.data.title").value(updatedTitle))
                    .andExpect(jsonPath("$.data.content").value(updatedContent));

            // Verify database
            Document updated = documentRepository.findById(existingNote.getId()).orElseThrow();
            assertEquals(updatedTitle, updated.getTitle());
            assertEquals(updatedContent, updated.getContent());
        }

        @Test
        void shouldReturn404WhenUpdatingNonExistentNote() throws Exception {
            // Arrange
            NoteUpsertRequest updateRequest = NoteUpsertRequest.builder()
                    .title("Updated Title").content("Updated Content").build();

            // Act & Assert
            mockMvc.perform(
                        patch(BASE_URI + "/99999")
                                .with(jwtWithUserId(TEST_USER_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldRejectUpdateWithNullTitle() throws Exception {
            // Arrange
            NoteUpsertRequest invalidRequest = NoteUpsertRequest.builder()
                    .title(null).content("Updated Content").build();

            // Act & Assert
            mockMvc.perform(
                        patch(BASE_URI + "/" + existingNote.getId())
                                .with(jwtWithUserId(TEST_USER_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest))
                    )
                    .andExpect(status().isBadRequest());

            // Verify unchanged
            Document unchanged = documentRepository.findById(existingNote.getId()).orElseThrow();
            assertEquals(existingNote.getTitle(), unchanged.getTitle());
        }

        @Test
        void shouldRejectUpdateWithNullContent() throws Exception {
            // Arrange
            NoteUpsertRequest invalidRequest = NoteUpsertRequest.builder()
                    .title("Updated Title").content(null).build();

            // Act & Assert
            mockMvc.perform(
                        patch(BASE_URI + "/" + existingNote.getId())
                                .with(jwtWithUserId(TEST_USER_ID))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest))
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            // Arrange
            NoteUpsertRequest updateRequest = NoteUpsertRequest.builder()
                    .title("Updated Title").content("Updated Content").build();

            // Act & Assert
            mockMvc.perform(
                        patch(BASE_URI + "/" + existingNote.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest))
                    )
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("DELETE /api/documents/notes/{id}")
    class DeleteNoteIntegrationTests {
        @Test
        void shouldDeleteExistingNote() throws Exception {
            // Arrange
            int initialSize = documentRepository.findAllByUserId(TEST_USER_ID).size();

            // Act & Assert
            mockMvc.perform(
                        delete("/api/documents/notes/" + existingNote.getId())
                                .with(jwtWithUserId(TEST_USER_ID))
                    )
                    .andExpect(status().isOk());

            // Verify deletion
            assertFalse(documentRepository.existsById(existingNote.getId()));
            assertEquals(initialSize - 1, documentRepository.findAllByUserId(TEST_USER_ID).size());
        }

        @Test
        void shouldReturn404WhenDeletingNonExistentNote() throws Exception {
            // Act & Assert
            mockMvc.perform(
                        delete("/api/documents/notes/99999")
                                .with(jwtWithUserId(TEST_USER_ID))
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturn401WhenNotAuthenticated() throws Exception {
            // Act & Assert
            mockMvc.perform(delete("/api/documents/notes/" + existingNote.getId()))
                    .andExpect(status().isUnauthorized());
        }
    }
}
