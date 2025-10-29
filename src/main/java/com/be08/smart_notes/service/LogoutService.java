package com.be08.smart_notes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ACCESS_TOKEN_BLACKLIST_KEY_PREFIX = "jwt:access_token_blacklist:";
    private static final String REFRESH_TOKEN_BLACKLIST_KEY_PREFIX = "jwt:refresh_token_blacklist:";

    /**
     * Blacklist the given JWT access token by storing its ID (jti) in Redis with an expiration time.
     * @param jwt the JWT access token to blacklist
     */
    public void blacklistAccessToken(Jwt jwt){
        String jti = jwt.getId();
        Instant expirationTime = jwt.getExpiresAt();

        if(jti == null || expirationTime == null){
            log.warn("Cannot blacklist access token: JWT ID (jti) not found");
            return;
        }

        // Calculate the TTL (Time-to-live) for the blacklist entry
        Duration ttl = Duration.between(Instant.now(), expirationTime);

        // If the token is already expired, no need to blacklist
        if(ttl.isNegative()){
            log.warn("Cannot blacklist access token with jti {}: Token already expired", jti);
            return;
        }

        // Store the jti in Redis with the calculated TTL
        String redisKey = ACCESS_TOKEN_BLACKLIST_KEY_PREFIX + jti;

        // Using a simple string value to indicate blacklisting
        // Set TTL to automatically remove the entry after expiration
        redisTemplate.opsForValue().set(redisKey, "invalid", ttl);

        log.info("Access token with jti {} has been blacklisted. Valid until {}", jti, jwt.getExpiresAt());
    }

    /**
     * Blacklist the given JWT refresh token by storing its ID (jti) in Redis with an expiration time.
     * @param jwt the JWT refresh token to blacklist
     */
    public void blacklistRefreshToken(Jwt jwt){
        String jti = jwt.getId();
        Instant expirationTime = jwt.getExpiresAt();

        if(jti == null || expirationTime == null){
            log.warn("Cannot blacklist refresh token: JWT ID (jti) not found");
            return;
        }

        // Calculate TTL for the blacklist entry
        Duration ttl = Duration.between(Instant.now(), expirationTime);

        // If the token is already expired, no need to blacklist
        if(ttl.isNegative()){
            log.warn("Cannot blacklist refresh token with jti {}: Token already expired", jti);
            return;
        }

        // Store the jti in Redis with the calculated TTL
        String redisKey = REFRESH_TOKEN_BLACKLIST_KEY_PREFIX + jti;

        // Using a simple string value to indicate blacklisting
        // Set TTL to automatically remove the entry after expiration
        redisTemplate.opsForValue().set(redisKey, "invalid", ttl);

        log.info("Refresh token with jti {} has been blacklisted. Valid until {}", jti, jwt.getExpiresAt());
    }

    /**
     * Check if the given access token ID (jti) is in the Redis blacklist.
     * @param tokenId the JWT ID (jti) of the access token
     * @return true if the token ID is blacklisted, false otherwise
     */
    public boolean isAccessTokenBlacklisted(String tokenId){
        String redisKey = ACCESS_TOKEN_BLACKLIST_KEY_PREFIX + tokenId;
        return redisTemplate.hasKey(redisKey);
    }

    /**
     * Check if the given refresh token ID (jti) is in the Redis blacklist.
     * @param tokenId the JWT ID (jti) of the refresh token
     * @return true if the token ID is blacklisted, false otherwise
     */
    public boolean isRefreshTokenBlacklisted(String tokenId) {
        String redisKey = REFRESH_TOKEN_BLACKLIST_KEY_PREFIX + tokenId;
        return redisTemplate.hasKey(redisKey);
    }
}
