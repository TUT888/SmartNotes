package com.be08.smart_notes.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"password"})
public class LoginRequest {
    @NotBlank(message = "EMAIL_EMPTY")
    @Email(message = "INVALID_EMAIL_FORMAT")
    String email;

    @NotBlank(message = "PASSWORD_EMPTY")
    String password;
}
