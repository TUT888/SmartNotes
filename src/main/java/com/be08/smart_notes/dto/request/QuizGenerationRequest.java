package com.be08.smart_notes.dto.request;

import com.be08.smart_notes.validation.group.MultipleDocument;
import com.be08.smart_notes.validation.group.SingleDocument;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizGenerationRequest {
    @NotNull(groups = SingleDocument.class, message = "DOCUMENT_ID_REQUIRED")
    private Integer docId;

    @NotNull(groups = MultipleDocument.class, message = "DOCUMENT_IDS_REQUIRED")
    @Size(min = 1, max = 5, message = "QUIZ_DOCUMENT_SIZE_EXCEED")
    private List<Integer> docIds;

    @Builder.Default
    @Min(value = 1, message = "INVALID_QUIZ_SIZE")
    @Max(value = 20, message = "INVALID_QUIZ_SIZE")
    private Integer sizeOfEachQuiz = 10;
}
