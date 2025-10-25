package com.be08.smart_notes.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.LinkedList;
import java.util.List;

@Configuration
public class JwtTokenConfig {
    @Value("${jwt.keystore.location}")
    private Resource keyStoreResource;

    @Value("${jwt.keystore.password}")
    private String keyStorePassword;

    @Value("${jwt.key.password}")
    private String keyPassword;

    // Alias of the key used to SIGN new tokens (CURRENT key)
    @Value("${jwt.signing.key.alias}")
    private String signingKeyAlias;

    // List of aliases used to VERIFY old/current tokens
    @Value("${jwt.verification.key.aliases}")
    private String[] verificationKeyAliases;

    /**
     * Loads ALL keys from the KeyStore specified in 'verificationKeyAliases'
     * and wraps them in a JWKSource for validation purposes (Key Rotation).
     * @return JWKSource containing all Public Keys for verification
     */
    @Bean
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource() {
        List<JWK> jwkList = new LinkedList<>();

        try (InputStream inputStream = keyStoreResource.getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, keyStorePassword.toCharArray());

            for (String alias : verificationKeyAliases) {
                Key key = keyStore.getKey(alias, keyPassword.toCharArray());
                Certificate cert = keyStore.getCertificate(alias);

                if (key instanceof PrivateKey && cert != null) {
                    // Create RSAKey with both Public and Private Key
                    RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) cert.getPublicKey())
                            .privateKey((RSAPrivateKey) key)
                            .keyID(alias) // Use alias as Key ID (kid) for JWS Header
                            .build();
                    jwkList.add(rsaKey);
                }
            }
        }

        if (jwkList.isEmpty()) {
            throw new IllegalStateException("No valid JWK found for signing or verification.");
        }

        // Returns an ImmutableJWKSet containing all loaded keys for verification
        return new ImmutableJWKSet<>(new JWKSet(jwkList));
    }

    /**
     * Creates a KeyPair bean containing ONLY the current signing key.
     * This KeyPair is used to extract the PrivateKey for signing new tokens.
     * @return KeyPair containing the current signing key
     */
    @Bean
    @SneakyThrows
    public KeyPair signingKeyPair() {
        try (InputStream inputStream = keyStoreResource.getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, keyStorePassword.toCharArray());

            Key key = keyStore.getKey(signingKeyAlias, keyPassword.toCharArray());
            Certificate cert = keyStore.getCertificate(signingKeyAlias);

            if (key instanceof PrivateKey && cert != null) {
                return new KeyPair(cert.getPublicKey(), (PrivateKey) key);
            }
        }
        throw new IllegalStateException("Signing KeyPair not found or invalid for alias: " + signingKeyAlias);
    }

    /**
     * JwtEncoder (used for creating new tokens):
     * Uses the Private Key from the signingKeyPair() for signing.
     * @param signingKeyPair KeyPair containing the current signing key
     * @return JwtEncoder for signing new tokens
     */
    @Bean
    JwtEncoder jwtEncoder(KeyPair signingKeyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) signingKeyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) signingKeyPair.getPrivate();

        // The key ID is crucial for the JwtEncoder to sign the token with the correct 'kid'
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(signingKeyAlias)
                .build();

        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(rsaKey));
        return new NimbusJwtEncoder(jwks);
    }

    /**
     * JwtDecoder (used for verifying tokens):
     * Uses the list of Public Keys from the jwkSource() for verification (Key Rotation).
     * @param jwkSource JWKSource containing all Public Keys for verification
     * @return JwtDecoder for verifying tokens
     */
    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {

        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

        // Configures the processor to use the JWKSource for key selection based on JWS header 'kid'
        JWSKeySelector<SecurityContext> keySelector =
                new JWSVerificationKeySelector<>(
                        JWSAlgorithm.RS256, jwkSource);
        jwtProcessor.setJWSKeySelector(keySelector);

        // This is the correct way to wrap the Nimbus processor in the Spring JwtDecoder
        return new NimbusJwtDecoder(jwtProcessor);
    }
}
