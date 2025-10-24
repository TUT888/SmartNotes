package com.be08.smart_notes.exception;

import com.be08.smart_notes.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){
        log.error("Exception: ", exception);

        int code = ErrorCode.UNCATEGORIZED.getCode();
        String message = ErrorCode.UNCATEGORIZED.getMessage();

        ApiResponse apiResponse = ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        log.error("AppException: ", exception);

        ErrorCode errorCode = exception.getErrorCode();
        int code = errorCode.getCode();
        String message = errorCode.getMessage();

        ApiResponse apiResponse = ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidationException(MethodArgumentNotValidException exception){
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        int code = errorCode.getCode();
        String message = errorCode.getMessage();

        ApiResponse apiResponse = ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
