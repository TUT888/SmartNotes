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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Inject the key alias to be used for signing tokens
    @Value("${jwt.signing.key.alias}")
    private String signingKeyAlias;

    /**
     * Authenticate user and generate JWT tokens
     * @param request
     * @return AuthenticationResponse containing access and refresh tokens
     */
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

        // Create Access Token and Refresh Token, passing the key alias
        String accessToken = jwtService.generateAccessToken(user, signingKeyAlias);
        String refreshToken = jwtService.generateRefreshToken(user, signingKeyAlias);

        log.info("User with email {} authenticated successfully", email);
        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
