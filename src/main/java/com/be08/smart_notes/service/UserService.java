package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.UserCreationRequest;
import com.be08.smart_notes.dto.response.UserResponse;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.mapper.UserMapper;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request){
        String email = request.getEmail();

        if(userRepository.existsByEmail(email)){
            log.error("User with email {} already exists", email);
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setCreatedAt(LocalDateTime.now());

        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        log.info("User with email {} registered successfully", email);

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
