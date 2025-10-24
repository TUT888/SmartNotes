package com.be08.smart_notes.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ERROR_CODE_KEY(9998, "Invalid error code key", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTS(2001, "User already exists", HttpStatus.BAD_REQUEST),
    NAME_EMPTY(2002, "Name cannot be empty", HttpStatus.BAD_REQUEST),
    EMAIL_EMPTY(2003, "Email cannot be empty", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(2004, "Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_SIZE(2005, "Email must be less than 255 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY(2006, "Password cannot be empty", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_SIZE(2007, "Password must be at least 8 characters long", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_PATTERN(2008, "Password must contain at least one uppercase letter, one lowercase letter, and one digit, and no whitespace", HttpStatus.BAD_REQUEST)
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
