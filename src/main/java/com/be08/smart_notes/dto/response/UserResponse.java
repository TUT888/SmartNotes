package com.be08.smart_notes.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String name;
    String email;
    String avatarUrl;

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now(); // Default to current time
    LocalDateTime updatedAt;
}
