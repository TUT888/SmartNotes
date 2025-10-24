package com.be08.smart_notes.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @NotBlank(message = "NAME_EMPTY")
    String name;

    @NotBlank(message = "EMAIL_EMPTY")
    @Email(message = "INVALID_EMAIL_FORMAT")
    @Size(max = 255, message = "INVALID_EMAIL_SIZE")
    String email;

    @NotBlank(message = "PASSWORD_EMPTY")
    @Size(min = 8, message = "INVALID_PASSWORD_SIZE")
    @Pattern(regexp = "^(?=\\S+$)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).+$", message = "INVALID_PASSWORD_PATTERN")
    String password;
}
