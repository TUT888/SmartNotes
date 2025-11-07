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
    // 99xx - General errors
    UNCATEGORIZED(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    // Used when an invalid error code key is provided
    INVALID_ERROR_CODE_KEY(9998, "Invalid error code key", HttpStatus.INTERNAL_SERVER_ERROR),
    // Used when authentication is required but not provided
    UNAUTHORIZED(9997, "Unauthorized: Full authentication is required to access this resource", HttpStatus.UNAUTHORIZED),

    // 20xx - User-related errors
    USER_EXISTS(2001, "User already exists", HttpStatus.BAD_REQUEST),
    NAME_EMPTY(2002, "Name cannot be empty", HttpStatus.BAD_REQUEST),
    EMAIL_EMPTY(2003, "Email cannot be empty", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(2004, "Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_SIZE(2005, "Email must be less than 255 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY(2006, "Password cannot be empty", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_SIZE(2007, "Password must be at least 8 characters long", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_PATTERN(2008, "Password must contain at least one uppercase letter, one lowercase letter, and one digit, and no whitespace", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(2009, "User not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(2010, "Unauthenticated access", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(2011, "Access denied", HttpStatus.FORBIDDEN),

    // 21xx - Token-related errors
    REFRESH_TOKEN_EMPTY(2102, "Refresh token cannot be empty", HttpStatus.BAD_REQUEST),

    // 22xx - Document-related features
    DOCUMENT_NOT_FOUND(2201, "Document not found", HttpStatus.NOT_FOUND),
    DOCUMENT_ID_REQUIRED(2202, "Single document ID required", HttpStatus.BAD_REQUEST),
    DOCUMENT_IDS_REQUIRED(2203, "List of document IDs required", HttpStatus.BAD_REQUEST),
    NOTE_CONTENT_EMPTY(2204, "Note content cannot be empty", HttpStatus.BAD_REQUEST),
    SYSTEM_SOURCE_DOCUMENT_NOT_FOUND(2205, "System source document not found", HttpStatus.NOT_FOUND),
    DOCUMENT_CANNOT_BE_DELETED(2206, "This document cannot be deleted", HttpStatus.BAD_REQUEST),

    // 23xx - AI-related features
    FAILED_INFERENCE_REQUEST(2301, "AI inference request failed", HttpStatus.BAD_GATEWAY),

    // 24xx - Quiz-related features
    QUIZ_NOT_FOUND(2401, "Quiz not found", HttpStatus.NOT_FOUND),
    QUIZ_DOCUMENT_SIZE_EXCEED(2402, "Number of quiz IDs must be between 1 and 5", HttpStatus.BAD_REQUEST),
    INVALID_QUIZ_SIZE(2403, "Total number of questions must between 1 and 20", HttpStatus.BAD_REQUEST),


    // 25xx - Flashcard-related errors
    FLASHCARD_FRONT_CONTENT_REQUIRED(2501, "Flashcard front content is required", HttpStatus.BAD_REQUEST),
    FLASHCARD_BACK_CONTENT_REQUIRED(2502, "Flashcard back content is required", HttpStatus.BAD_REQUEST),
    FLASHCARD_SOURCE_DOCUMENT_ID_REQUIRED(2503, "Flashcard source document ID is required", HttpStatus.BAD_REQUEST),
    FLASHCARD_NOT_FOUND(2504, "Flashcard not found", HttpStatus.NOT_FOUND),

    // 26xx - Flashcard Set-related errors
    FLASHCARD_SET_TITLE_REQUIRED(2601, "Flashcard set title is required", HttpStatus.BAD_REQUEST),
    FLASHCARD_SET_TITLE_TOO_LONG(2602, "Flashcard set title is too long", HttpStatus.BAD_REQUEST),
    INVALID_FLASHCARD_SET_TITLE(2603, "Invalid flashcard set title", HttpStatus.BAD_REQUEST),
    FLASHCARD_SET_NOT_FOUND(2604, "Flashcard set not found", HttpStatus.NOT_FOUND),
    FLASHCARD_SET_CANNOT_BE_MODIFIED(2605, "This flashcard set cannot be modified", HttpStatus.BAD_REQUEST),
    FLASHCARD_SET_CANNOT_BE_DELETED(2606, "This flashcard set cannot be deleted", HttpStatus.BAD_REQUEST),

    // 27xx - Quiz-related features
    QUIZ_SET_NOT_FOUND(2701, "Quiz set not found", HttpStatus.NOT_FOUND),
    QUIZ_SET_ID_REQUIRED(2702, "Quiz set ID required", HttpStatus.BAD_REQUEST),
    QUIZ_SET_TITLE_REQUIRED(2703, "Quiz set title required", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
