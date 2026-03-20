package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.dto.response.UserResponse;
import com.be08.smart_notes.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User", description = "All operations for user-related features")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    UserService userService;

    /**
     * Get details of the currently authenticated user
     * @return ApiResponse containing UserResponse
     */
    @GetMapping("/me")
    @Operation(summary = "Get user detail")
    public ApiResponse<UserResponse> getMe(){
        UserResponse response = userService.getMe();

        return ApiResponse.<UserResponse>builder()
                .message("User details retrieved successfully!")
                .data(response)
                .build();
    }
}
