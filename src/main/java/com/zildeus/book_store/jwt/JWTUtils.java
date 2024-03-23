package com.zildeus.book_store.jwt;

import com.zildeus.book_store.service.ApplicationUserDetailsService;
import com.zildeus.book_store.repository.JWTRefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Objects;

@RequiredArgsConstructor
public class JWTUtils {
    private final JWTRefreshTokenRepository repository;
    private final ApplicationUserDetailsService detailsService;
    private final JwtDecoder decoder;
    public boolean IsRefreshTokenInDatabase(String refreshToken){
        return repository.findByRefreshToken(refreshToken).map((t)->!t.getRevoked()).isPresent();
    }
    public Jwt GetTokenFromHeader(HttpServletRequest request){
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader==null||!authHeader.startsWith("Bearer "))
            return null;
        final String token = authHeader.substring(7);
        return decoder.decode(token);
    }
    public boolean IsTokenExpired(Jwt token){
        return Objects.requireNonNull(token.getExpiresAt()).isBefore(Instant.now());
    }
    public boolean IsTokenValid(Jwt token){
        return !IsTokenExpired(token)&&detailsService.loadUserByUsername(token.getSubject())!=null;
    }
    public UserDetails GetTokenUser(Jwt token){
        return detailsService.loadUserByUsername(token.getSubject());
    }
}
