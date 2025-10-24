package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.UserCreationRequest;
import com.be08.smart_notes.dto.response.AuthenticationResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.UserMapper;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;

    public AuthenticationResponse register(UserCreationRequest request){
        String email = request.getEmail();
        log.info("Registering user with email: {}", email);
        String password = request.getPassword();

        // basic business-level check (defense in depth)
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }

        if(userRepository.existsByEmail(email)){
            log.error("User with email {} already exists", email);
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setCreatedAt(LocalDateTime.now());

        try {
            userRepository.save(user);

            return AuthenticationResponse.builder()
                    .isAuthenticated(true)
                    .build();
        } catch (DataIntegrityViolationException exception){
            // final safeguard for concurrent inserts — DB unique constraint
            log.error("Data integrity violation while creating user with email {}: {}", email, exception.getMessage());
        }

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .build();
    }
}
