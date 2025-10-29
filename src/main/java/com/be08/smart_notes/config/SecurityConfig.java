package com.be08.smart_notes.config;

import com.be08.smart_notes.common.SecurityConstants;
import com.be08.smart_notes.service.LogoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    public static class BlacklistFilter extends BearerTokenAuthenticationFilter {
        private final LogoutService logoutService;

        public BlacklistFilter(LogoutService logoutService){
            super((AuthenticationManagerResolver<HttpServletRequest>) authentication -> null);
            this.logoutService = logoutService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Check if the authentication principal is a JWT and has been decoded by the resource server with JwtDecoder
            if(authentication != null && authentication.getPrincipal() instanceof Jwt){
                Jwt jwt = (Jwt) authentication.getPrincipal();

                String tokenId = jwt.getId();

                // If the token ID is blacklisted, clear the security context and throw an exception
                if(logoutService.isAccessTokenBlacklisted(tokenId)){
                    // Clear the security context to prevent further processing
                    SecurityContextHolder.clearContext();

                    // Allow the CustomBearerTokenAuthenticationEntryPoint to handle the response
                    throw new BadCredentialsException("The Access Token is blacklisted");
                }
            }

            filterChain.doFilter(request, response);
        }
    }

    /**
     * Configure security filter chain
     * @param httpSecurity
     * @return Configured SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, ObjectMapper objectMapper, LogoutService logoutService) throws Exception {
        httpSecurity
                // Disable CSRF protection for stateless REST APIs
                .csrf(AbstractHttpConfigurer::disable)

                // Add custom BlacklistFilter after the AnonymousAuthenticationFilter
                .addFilterAfter(new BlacklistFilter(logoutService), AnonymousAuthenticationFilter.class)

                // Define authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Permit all requests to public authentication endpoints
                        .requestMatchers(SecurityConstants.PUBLIC_ENDPOINTS)
                        .permitAll()

                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                )
                // Configure OAuth2 Resource Server to use JWT for authentication
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        // Custom authentication entry point for handling auth errors
                        .authenticationEntryPoint(new CustomBearerTokenAuthenticationEntryPoint(objectMapper()))
                )
                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }
}
