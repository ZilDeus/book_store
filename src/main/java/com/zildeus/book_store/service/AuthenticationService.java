package com.zildeus.book_store.service;

import com.zildeus.book_store.config.jwt.JWTTokenGenerator;
import com.zildeus.book_store.dto.AuthResponseDto;
import com.zildeus.book_store.dto.TokenType;
import com.zildeus.book_store.dto.UserRegistrationRequest;
import com.zildeus.book_store.exceptions.DuplicateResourceException;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
import com.zildeus.book_store.model.ApplicationUser;
import com.zildeus.book_store.model.JWTRefreshToken;
import com.zildeus.book_store.model.UserType;
import com.zildeus.book_store.repository.ApplicationUserRepository;
import com.zildeus.book_store.repository.JWTRefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ApplicationUserRepository repository;
    private final JWTTokenGenerator tokenGenerator;
    private final JWTRefreshTokenRepository tokenRepository;
    private final PasswordEncoder encoder;

    private Authentication CreateAuthentication(ApplicationUser userInfoEntity) {
        String username = userInfoEntity.getUsername();
        String password = userInfoEntity.getPassword();
        String type = userInfoEntity.getType().toString();
        return new UsernamePasswordAuthenticationToken(username, password, List.of(new SimpleGrantedAuthority(type)));
    }
    private void SaveRefreshToken(ApplicationUser user,String token){
        JWTRefreshToken refreshToken = new JWTRefreshToken();
        refreshToken.setRefreshToken(token);
        refreshToken.setRevoked(false);
        refreshToken.setOwner(user);
        tokenRepository.save(refreshToken);
    }
    private void CreateRefreshTokenCookie(HttpServletResponse response,String refreshToken)
    {
        Cookie cookie = new Cookie("refresh_token",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(15*24*60*60);
        response.addCookie(cookie);
    }
    public AuthResponseDto RegisterNewUser(UserRegistrationRequest registrationRequest, HttpServletResponse response){
        try {
            if(repository.findByUsername(registrationRequest.username()).isPresent())
                throw new DuplicateResourceException("user already exists");
            ApplicationUser user = new ApplicationUser();
            user.setBalance(0.0f);
            user.setUsername(registrationRequest.username());
            user.setEmail(registrationRequest.email());
            user.setType(UserType.valueOf(registrationRequest.userType()));
            user.setPassword(encoder.encode(registrationRequest.password()));
            var authentication = CreateAuthentication(user);
            String accessToken = tokenGenerator.GenerateAccessToken(authentication);
            String refreshToken = tokenGenerator.GenerateRefreshToken(authentication);
            repository.save(user);
            SaveRefreshToken(user,refreshToken);
            CreateRefreshTokenCookie(response,refreshToken);
            return new AuthResponseDto(accessToken,
                    5*60,
                    TokenType.Bearer,
                    user.getUsername()
            );
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    public AuthResponseDto GetAccessTokenFromRefreshToken(Authentication authentication, HttpServletResponse response) {
        try {
            ApplicationUser user = repository.findByUsername(authentication.getName())
                    .orElseThrow(()->
                            new ResourceNotFoundException("USER NOT FOUND!")
                    );

            String accessToken = tokenGenerator.GenerateAccessToken(authentication);
            String refreshToken = tokenGenerator.GenerateRefreshToken(authentication);
            SaveRefreshToken(user,refreshToken);
            CreateRefreshTokenCookie(response,refreshToken);
            return new AuthResponseDto(accessToken,
                    5*60,
                    TokenType.Bearer,
                    user.getUsername()
            );
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}
