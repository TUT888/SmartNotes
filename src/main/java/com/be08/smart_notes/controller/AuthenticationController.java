package com.be08.smart_notes.controller;

import com.be08.smart_notes.dto.request.UserCreationRequest;
import com.be08.smart_notes.dto.response.ApiResponse;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.service.UserService;
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
    UserService userService;

    @PostMapping("/register")
    ApiResponse<User> register(@RequestBody UserCreationRequest request){
        var user = userService.createUser(request);
        return ApiResponse.<User>builder()
                .data(user)
                .build();
    }
}
