package com.be08.smart_notes.service;

import com.be08.smart_notes.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtService {
    JwtEncoder jwtEncoder;

    // Access token valid for 15 minutes
    @Value("${jwt.access-token.expiration-minutes")
    private final long ACCESS_TOKEN_EXPIRATION; // in minute

    // Refresh token valid for 7 days
    @Value("${jwt.refresh-token.expiration-days")
    private final long REFRESH_TOKEN_EXPIRATION; // in days

    /**
     * Generate Token
     * @param user
     * @param expirationDuration
     * @param unit
     * @return Generated JWT token as String
     */
    public String generateToken(User user, long expirationDuration, ChronoUnit unit){
        Instant now = Instant.now();

        // Build the JWT claims set for payload
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("smartnotes-auth-server")
                .issuedAt(now)
                .expiresAt(now.plus(expirationDuration, unit))
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("scope", "USER")
                .build();

        // Encode the JWT with the claims set
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    /**
     * Generate Access Token
     * @param user
     * @return Generated Access JWT token as String
     */
    public String generateAccessToken(User user){
        return generateToken(user, ACCESS_TOKEN_EXPIRATION, ChronoUnit.MINUTES);
    }

    /**
     * Generate Refresh Token
     * @param user
     * @return Generated Refresh JWT token as String
     */
    public String generateRefreshToken(User user){
        return generateToken(user, REFRESH_TOKEN_EXPIRATION, ChronoUnit.DAYS);
    }
}
