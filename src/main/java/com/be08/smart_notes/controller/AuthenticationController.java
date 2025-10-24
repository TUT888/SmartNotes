package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.LoginRequest;
import com.be08.smart_notes.dto.request.UserCreationRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.AuthenticationResponse;
import com.be08.smart_notes.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/register")
    ApiResponse<AuthenticationResponse> register(@RequestBody @Valid UserCreationRequest request){
        AuthenticationResponse response = authenticationService.register(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("User registered successfully")
                .data(response)
                .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request){
        AuthenticationResponse response = authenticationService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("User logged in successfully")
                .data(response)
                .build();
    }
}
