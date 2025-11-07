package com.be08.smart_notes.dto.request;

import com.be08.smart_notes.validation.group.MultipleDocument;
import com.be08.smart_notes.validation.group.SingleDocument;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizGenerationRequest {
    @NotNull(groups = SingleDocument.class, message = "DOCUMENT_ID_REQUIRED")
    Integer docId;

    @NotNull(groups = MultipleDocument.class, message = "DOCUMENT_IDS_REQUIRED")
    @Size(min = 1, max = 5, message = "QUIZ_DOCUMENT_SIZE_EXCEED")
    List<Integer> docIds;

    @Builder.Default
    @Min(value = 1, message = "INVALID_QUIZ_SIZE")
    @Max(value = 20, message = "INVALID_QUIZ_SIZE")
    Integer sizeOfEachQuiz = 10;
}
