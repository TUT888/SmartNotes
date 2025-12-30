package com.be08.smart_notes.helper;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class JwtBuilder {
    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtWithUserId(int userId) {
        return jwt().jwt(builder -> builder
                .issuer("smart-notes-auth-server")
                .subject(String.valueOf(userId))
                .claim("email", "example@gmail.com")
                .claim("scope", "USER").build());
    }
}
