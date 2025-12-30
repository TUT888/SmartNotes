package com.be08.smart_notes.service;

import com.be08.smart_notes.model.User;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {

    private final JwtEncoder jwtEncoder;

    // Injected from application.properties
    @Value("${jwt.access-token.expiration-minutes}")
    private long ACCESS_TOKEN_EXPIRATION_MINUTES;

    @Value("${jwt.refresh-token.expiration-days}")
    private long REFRESH_TOKEN_EXPIRATION_DAYS;

    /**
     * Generates a JWT token for the given user with specified expiration and key ID.
     * @param user
     * @param expirationDuration
     * @param unit
     * @param keyId
     * @return Generated JWT token as String
     */
    public String generateToken(User user, long expirationDuration, ChronoUnit unit, String keyId) {
        Instant now = Instant.now();

        // Unique Token ID (jti) for token identification
        String jti = UUID.randomUUID().toString();

        // 1. Define claims (Payload)
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("smart-notes-auth-server")
                .issuedAt(now)
                .expiresAt(now.plus(expirationDuration, unit))
                .subject(String.valueOf(user.getId())) // Subject is User ID
                .id(jti) // Unique JWT Token ID
                .claim("email", user.getEmail())
                .claim("scope", "USER")
                .build();

        // 2. Add Key ID (kid) header parameter to identify the signing key
        // This is crucial for Key Rotation to work in JwtDecoder
        JwtEncoderParameters encoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(SignatureAlgorithm.RS256).keyId(keyId).build(),
                claims
        );

        // 3. Encode and Sign the token
        return this.jwtEncoder.encode(encoderParameters).getTokenValue();
    }

    /**
     * Access Token generation
     * @param user
     * @param keyId
     * @return Generated Access Token as String
     */
    public String generateAccessToken(User user, String keyId) {
        return generateToken(user, ACCESS_TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES, keyId);
    }

    /**
     * Refresh Token generation
     * @param user
     * @param keyId
     * @return Generated Refresh Token as String
     */
    public String generateRefreshToken(User user, String keyId) {
        return generateToken(user, REFRESH_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS, keyId);
    }
}
