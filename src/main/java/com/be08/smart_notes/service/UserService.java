package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.UserCreationRequest;
import com.be08.smart_notes.exception.AppException;
import com.be08.smart_notes.exception.ErrorCode;
import com.be08.smart_notes.model.User;
import com.be08.smart_notes.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;

    public User createUser(UserCreationRequest request){
        String email = request.getEmail();

        if(userRepository.existsByEmail(email)){
            log.error("User with email {} already exists", email);
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        String name = request.getName();
        String password = request.getPassword();

        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        return userRepository.save(user);
    }
}
