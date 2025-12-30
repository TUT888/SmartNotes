package com.be08.smart_notes.integration.repository;

import com.be08.smart_notes.helper.DocumentDataBuilder;
import com.be08.smart_notes.model.Document;
import com.be08.smart_notes.repository.DocumentRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
@DisplayName("Document Repository Test")
@Tag("document")
@Tag("note")
public class DocumentRepositoryTest {
    @Autowired
    DocumentRepository documentRepository;

    int FIRST_USER_ID = 100;
    int SECOND_USER_ID = 101;

    Document document1;
    Document document2;
    Document document3;
    Document documentAnotherUser;

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();

        document1 = documentRepository.save(DocumentDataBuilder.createMockNote(FIRST_USER_ID).build());
        document2 = documentRepository.save(DocumentDataBuilder.createMockNote(FIRST_USER_ID).build());
        document3 = documentRepository.save(DocumentDataBuilder.createMockNote(FIRST_USER_ID).build());
        documentAnotherUser = documentRepository.save(DocumentDataBuilder.createMockNote(SECOND_USER_ID).build());
    }

    // --- findAllByUserId --- //
    @Nested
    @DisplayName("findAllByUserId(): List<Document>")
    class FindAllByUserIdTest {
        @Test
        void shouldReturnAllDocumentsWhenUserHasDocuments() {
            // Act
            List<Document> result = documentRepository.findAllByUserId(FIRST_USER_ID);

            // Assert
            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.stream().allMatch(doc -> doc.getUserId() == FIRST_USER_ID));
        }

        @Test
        void shouldReturnEmptyListWhenUserHasNoDocuments() {
            // Arrange
            int nonExistentUserId = 999;

            // Act
            List<Document> result = documentRepository.findAllByUserId(nonExistentUserId);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldNotReturnOtherUsersDocuments() {
            // Act
            List<Document> result = documentRepository.findAllByUserId(FIRST_USER_ID);

            // Assert
            assertNotNull(result);
            assertFalse(result.stream().anyMatch(doc -> doc.getUserId() == SECOND_USER_ID));
        }
    }

    // --- findByIdAndUserId --- //
    @Nested
    @DisplayName("findByIdAndUserId(): Optional<Document>")
    class FindByIdAndUserIdTest {
        @Test
        void shouldReturnDocumentWhenDocumentExists() {
            // Act
            Optional<Document> result = documentRepository.findByIdAndUserId(document1.getId(), FIRST_USER_ID);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(document1.getId(), result.get().getId());
            assertEquals(FIRST_USER_ID, result.get().getUserId());
        }

        @Test
        void shouldReturnEmptyWhenDocumentNotExists() {
            // Arrange
            int nonExistentId = 999;

            // Act
            Optional<Document> result = documentRepository.findByIdAndUserId(nonExistentId, FIRST_USER_ID);

            // Assert
            assertFalse(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenDocumentExistsButWrongUser() {
            // Act
            Optional<Document> result = documentRepository.findByIdAndUserId(document1.getId(), SECOND_USER_ID);

            // Assert
            assertFalse(result.isPresent());
        }

        @Test
        void shouldReturnEmptyWhenUserNotExists() {
            // Arrange
            int nonExistentUserId = 999;

            // Act
            Optional<Document> result = documentRepository.findByIdAndUserId(document1.getId(), nonExistentUserId);

            // Assert
            assertFalse(result.isPresent());
        }
    }

    // --- findFirstByTitleAndUserId --- //
    @Nested
    @DisplayName("findFirstByTitleAndUserId(): Optional<Document>")
    class FindFirstByTitleAndUserIdTest {
        @Test
        void shouldReturnDocumentWhenDocumentExists() {
            // Arrange
            String existingTitle = document1.getTitle();

            // Act
            Optional<Document> result = documentRepository.findFirstByTitleAndUserId(existingTitle, FIRST_USER_ID);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(existingTitle, result.get().getTitle());
            assertEquals(FIRST_USER_ID, result.get().getUserId());
        }

        @Test
        void shouldReturnEmptyWhenTitleNotExists() {
            // Arrange
            String nonExistentTitle = "Non Existent Title";

            // Act
            Optional<Document> result = documentRepository.findFirstByTitleAndUserId(nonExistentTitle, FIRST_USER_ID);

            // Assert
            assertFalse(result.isPresent());
        }

        @Test
        void shouldReturnOnlyRequestedUserWhenMultipleUsersHaveSameTitle() {
            // Arrange
            String sharedTitle = "Shared Title";
            Document doc1 = documentRepository.save(
                    DocumentDataBuilder.createMockNote(FIRST_USER_ID).title(sharedTitle).build()
            );
            Document doc2 = documentRepository.save(
                    DocumentDataBuilder.createMockNote(SECOND_USER_ID).title(sharedTitle).build()
            );

            // Act
            Optional<Document> result = documentRepository.findFirstByTitleAndUserId(sharedTitle, FIRST_USER_ID);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(FIRST_USER_ID, result.get().getUserId());
            assertEquals(doc1.getId(), result.get().getId());
        }
    }

    // --- findAllByIdIn --- //
    @Nested
    @DisplayName("findAllByIdIn(): List<Document>")
    class FindAllByIdInTest {
        @Test
        void shouldReturnAllDocumentsWhenAllIdsExist() {
            // Arrange
            List<Integer> ids = Arrays.asList(document1.getId(), document2.getId());

            // Act
            List<Document> result = documentRepository.findAllByIdIn(ids);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(doc -> doc.getId().equals(document1.getId())));
            assertTrue(result.stream().anyMatch(doc -> doc.getId().equals(document2.getId())));
        }

        @Test
        void shouldReturnOnlyExistingDocumentsWhenSomeIdsNotExist() {
            // Arrange
            List<Integer> ids = Arrays.asList(document1.getId(), 999, 998);

            // Act
            List<Document> result = documentRepository.findAllByIdIn(ids);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(document1.getId(), result.get(0).getId());
        }

        @Test
        void shouldReturnEmptyListWhenNoIdsExist() {
            // Arrange
            List<Integer> ids = Arrays.asList(999, 998, 997);

            // Act
            List<Document> result = documentRepository.findAllByIdIn(ids);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyListWhenGivenEmptyIdList() {
            // Arrange
            List<Integer> emptyIds = Collections.emptyList();

            // Act
            List<Document> result = documentRepository.findAllByIdIn(emptyIds);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnDocumentsFromMultipleUsers() {
            // Arrange
            List<Integer> ids = Arrays.asList(document1.getId(), documentAnotherUser.getId());

            // Act
            List<Document> result = documentRepository.findAllByIdIn(ids);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(doc -> doc.getUserId() == FIRST_USER_ID));
            assertTrue(result.stream().anyMatch(doc -> doc.getUserId() == SECOND_USER_ID));
        }

        @Test
        void shouldReturnSingleDocumentWhenGivenSingleId() {
            // Arrange
            List<Integer> ids = Collections.singletonList(document1.getId());

            // Act
            List<Document> result = documentRepository.findAllByIdIn(ids);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(document1.getId(), result.get(0).getId());
        }
    }

    // --- findAllByUserIdAndIdIn --- //
    @Nested
    @DisplayName("findAllByUserAndIdIn(): List<Document>")
    class FindAllByUserIdAndIdIn {
        @Test
        void shouldReturnAllDocumentsWhenAllIdsExistForUser() {
            // Arrange
            List<Integer> ids = Arrays.asList(document1.getId(), document2.getId());

            // Act
            List<Document> result = documentRepository.findAllByUserIdAndIdIn(FIRST_USER_ID, ids);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(doc -> doc.getUserId() == FIRST_USER_ID));
        }

        @Test
        void shouldReturnOnlyExistingWhenSomeIdsNotExistForUser() {
            // Arrange
            List<Integer> ids = Arrays.asList(document1.getId(), 999);

            // Act
            List<Document> result = documentRepository.findAllByUserIdAndIdIn(FIRST_USER_ID, ids);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(document1.getId(), result.get(0).getId());
        }

        @Test
        void shouldReturnEmptyListWhenNoIdsExistForUser() {
            // Arrange
            List<Integer> ids = Arrays.asList(999, 998);

            // Act
            List<Document> result = documentRepository.findAllByUserIdAndIdIn(FIRST_USER_ID, ids);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyListWhenGivenEmptyIdList() {
            // Arrange
            List<Integer> emptyIds = Collections.emptyList();

            // Act
            List<Document> result = documentRepository.findAllByUserIdAndIdIn(FIRST_USER_ID, emptyIds);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnEmptyListWhenDocumentsBelongToAnotherUser() {
            // Arrange - trying to get user 1's documents with user 2's ID
            List<Integer> ids = Arrays.asList(document1.getId(), document2.getId());

            // Act
            List<Document> result = documentRepository.findAllByUserIdAndIdIn(SECOND_USER_ID, ids);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldReturnOnlyRequestedUserDocumentsWhenGivenMixedUserDocuments() {
            // Arrange - mixing documents from different users
            List<Integer> ids = Arrays.asList(document1.getId(), documentAnotherUser.getId());

            // Act
            List<Document> result = documentRepository.findAllByUserIdAndIdIn(FIRST_USER_ID, ids);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(document1.getId(), result.get(0).getId());
            assertEquals(FIRST_USER_ID, result.get(0).getUserId());
        }

        @Test
        void shouldReturnSingleDocumentWhenGivenSingleId() {
            // Arrange
            List<Integer> ids = Collections.singletonList(document1.getId());

            // Act
            List<Document> result = documentRepository.findAllByUserIdAndIdIn(FIRST_USER_ID, ids);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(document1.getId(), result.get(0).getId());
        }

        @Test
        void shouldReturnEmptyListWhenUserNotExists() {
            // Arrange
            int nonExistentUserId = 999;
            List<Integer> ids = Arrays.asList(document1.getId(), document2.getId());

            // Act
            List<Document> result = documentRepository.findAllByUserIdAndIdIn(nonExistentUserId, ids);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
