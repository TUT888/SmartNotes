package com.be08.smart_notes.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class LogoutService {
    // In-memory store for blacklisted access token IDs (jti)
    private final Set<String> blacklistedAccessTokenIds = new HashSet<>();

    /**
     * Add the given access token's ID (jti) to the blacklist.
     * The token will be considered invalid for future requests.
     * @param jwt
     */
    public void blacklistAccessToken(Jwt jwt){
        String jti = jwt.getId();
        if(jti == null){
            log.warn("Cannot blacklist access token: JWT ID (jti) not found");
            return;
        }

        blacklistedAccessTokenIds.add(jti);
        log.info("Access token with jti {} has been blacklisted. Valid until {}", jti, jwt.getExpiresAt());
    }

    /**
     * Check if the given access token ID (jti) is in the blacklist.
     * @param tokenId
     * @return true if the token ID is blacklisted, false otherwise
     */
    public boolean isAccessTokenBlacklisted(String tokenId){
        return blacklistedAccessTokenIds.contains(tokenId);
    }
}
