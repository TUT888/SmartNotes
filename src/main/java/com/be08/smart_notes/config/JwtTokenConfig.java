package com.be08.smart_notes.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtTokenConfig {

    /**
     * Generate RSA Key Pair for JWT signing (private key) and verification (public key)
     * @return Generated RSA Key Pair
     */
    @Bean
    public KeyPair keyPair(){
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Key size
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Error: Failed to generate RSA key pair", exception);
        }
    }

    /**
     * Configure JwtEncoder using the generated RSA Key Pair for generating and signing JWT tokens
     * @param keyPair
     * @return Configured JwtEncoder
     */
    @Bean
    JwtEncoder jwtEncoder(KeyPair keyPair){
        // Extract public and private keys from the key pair
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // Create RSAKey object
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

        // Create JWKSource
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Configure JwtDecoder using the generated RSA Key Pair for validating and decoding JWT tokens
     * @param keyPair
     * @return Configured JwtDecoder
     */
    @Bean
    JwtDecoder jwtDecoder(KeyPair keyPair){
        // Extract public key from the key pair
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
