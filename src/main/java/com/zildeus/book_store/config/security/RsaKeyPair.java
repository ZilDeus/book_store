package com.zildeus.book_store.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties("jwt")
public record RsaKeyPair(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
