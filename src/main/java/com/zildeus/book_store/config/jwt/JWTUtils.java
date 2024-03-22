package com.zildeus.book_store.config.jwt;

import com.zildeus.book_store.config.user.UserDetailsService;
import com.zildeus.book_store.repository.JWTRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JWTUtils {
    private final JWTRefreshTokenRepository repository;
    public boolean IsRefreshTokenInDatabase(String refreshToken){
        return repository.findByRefreshToken(refreshToken).map((t)->!t.getRevoked()).isPresent();
    }
    public boolean IsTokenExpired(Jwt token){
        return Objects.requireNonNull(token.getExpiresAt()).isBefore(Instant.now());
    }
}
