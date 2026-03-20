package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.LoginRequest;
import com.be08.smart_notes.dto.request.RefreshTokenRequest;
import com.be08.smart_notes.dto.request.UserCreationRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.AuthenticationResponse;
import com.be08.smart_notes.dto.response.UserResponse;
import com.be08.smart_notes.service.AuthenticationService;
import com.be08.smart_notes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "Operation for authentication features")
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;

    /**
     * User Registration
     * @param request
     * @return ApiResponse containing UserResponse
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user")
    ApiResponse<UserResponse> register(@RequestBody @Valid UserCreationRequest request){
        UserResponse response = userService.createUser(request);
        return ApiResponse.<UserResponse>builder()
                .message("User registered successfully")
                .data(response)
                .build();
    }

    /**
     * User Login
     * @param request
     * @return ApiResponse containing AuthenticationResponse
     */
    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    ApiResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request){
        AuthenticationResponse response = authenticationService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("User logged in successfully")
                .data(response)
                .build();
    }

    /**
     * Refresh JWT Token
     * @param request
     * @return ApiResponse containing new AuthenticationResponse
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT Token")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request){
        AuthenticationResponse response = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("Token refreshed successfully")
                .data(response)
                .build();
    }

    /**
     * User Logout
     * Invalidate the current access token by adding its ID (jti) to the blacklist
     * @param authentication
     * @return ApiResponse with logout confirmation
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout and blacklist current token")
    @SecurityRequirement(name = "Bearer Authentication")
    ApiResponse<Void> logout(Authentication authentication){
        authenticationService.logout(authentication);

        return ApiResponse.<Void>builder()
                .message("Logged out successfully! Access token is now invalid.")
                .build();
    }
}
