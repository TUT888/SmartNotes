package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.LoginRequest;
import com.be08.smart_notes.dto.response.AuthenticationResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    public AuthenticationResponse login(LoginRequest request) {
        String email = request.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login Failed: User with email {} not found", email);
                    return new AppException(ErrorCode.UNAUTHENTICATED);
                });

        String rawPassword = request.getPassword();
        String hashedPassword = user.getPassword();

        if(!passwordEncoder.matches(rawPassword, hashedPassword)){
            log.warn("Login Failed: Invalid password for user with email {}", email);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        log.info("User with email {} authenticated successfully", email);
        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .build();
    }
}
