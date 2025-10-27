package com.be08.smart_notes.service;

import com.be08.smart_notes.dto.request.LoginRequest;
import com.be08.smart_notes.dto.request.RefreshTokenRequest;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtDecoder jwtDecoder;
    private final LogoutService logoutService;

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

    /**
     * Refresh JWT tokens using a valid refresh token
     * @param request
     * @return AuthenticationResponse containing new access and refresh tokens
     */
    public AuthenticationResponse refreshToken(RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();

        int userId;

        Jwt jwt;

        try {
            // Decode and validate the refresh token
            jwt = jwtDecoder.decode(refreshToken);

            if(logoutService.isRefreshTokenBlacklisted(jwt.getId())){
                log.warn("Refresh Token is blacklisted: jti {}", jwt.getId());
                throw new JwtException("Refresh Token has been revoked (blacklisted)");
            }

            // Extract user ID from token subject
            userId = Integer.parseInt(jwt.getSubject());
        } catch (JwtException exception) {
            log.warn("Refresh Token failed validation: {}", exception.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Refresh Token Failed: User with ID {} not found", userId);
                    return new AppException(ErrorCode.UNAUTHENTICATED);
                });

        String newAccessToken = jwtService.generateAccessToken(user, signingKeyAlias);
        String newRefreshToken = jwtService.generateRefreshToken(user, signingKeyAlias);

        log.info("New tokens generated successfully for user ID {}", userId);

        // Blacklist the previously used refresh token to prevent reuse
        logoutService.blacklistRefreshToken(jwt);

        return AuthenticationResponse.builder()
                .isAuthenticated(true)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    /**
     * Logout user by invalidating the current access token to blacklist its jti and prevent further use
     * @param authentication
     */
    public void logout(Authentication authentication){
        Jwt jwt = (Jwt) authentication.getPrincipal();
        logoutService.blacklistAccessToken(jwt);
    }
}
