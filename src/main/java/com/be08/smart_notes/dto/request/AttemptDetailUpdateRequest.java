package com.be08.smart_notes.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttemptDetailUpdateRequest {
    @NotNull(message = "ATTEMPT_DETAIL_ID_REQUIRED")
    Integer id;

    @NotNull(message = "ATTEMPT_DETAIL_ANSWER_REQUIRED")
    Character userAnswer;
}
