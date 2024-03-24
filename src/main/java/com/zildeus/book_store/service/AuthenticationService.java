package com.zildeus.book_store.service;

import com.zildeus.book_store.config.user.UserRole;
import com.zildeus.book_store.dto.AuthResponseDto;
import com.zildeus.book_store.dto.TokenType;
import com.zildeus.book_store.dto.UserRegistrationRequest;
import com.zildeus.book_store.exceptions.DuplicateResourceException;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
import com.zildeus.book_store.model.ApplicationUser;
import com.zildeus.book_store.model.JWTRefreshToken;
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
    private final JwtTokenGenerationService tokenGenerator;
    private final JWTRefreshTokenRepository tokenRepository;
    private final PasswordEncoder encoder;

    private Authentication CreateAuthentication(ApplicationUser applicationUser) {
        String username = applicationUser.getUsername();
        String password = applicationUser.getPassword();
        String type = applicationUser.getRoles().toString();
        return new UsernamePasswordAuthenticationToken(username, password, List.of(new SimpleGrantedAuthority(type)));
    }
    private void RegisterRefreshToken(ApplicationUser user, String token){
        JWTRefreshToken refreshToken = new JWTRefreshToken();
        refreshToken.setRefreshToken(token);
        refreshToken.setRevoked(false);
        refreshToken.setOwner(user);
        tokenRepository.save(refreshToken);
    }
    private void WriteAccessTokenToCookies(HttpServletResponse response, String refreshToken)
    {
        Cookie cookie = new Cookie("refresh_token",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(15*24*60*60);
        response.addCookie(cookie);
    }
    public String RegisterNewUser(UserRegistrationRequest registrationRequest){
        try {
            if(repository.findByUsername(registrationRequest.username()).isPresent())
                throw new DuplicateResourceException("user already exists");
            ApplicationUser user = new ApplicationUser();
            user.setBalance(0.0f);
            user.setUsername(registrationRequest.username());
            user.setEmail(registrationRequest.email());
            user.setRoles(registrationRequest.userRoles().stream().map(UserRole::valueOf).toList());
            user.setPassword(encoder.encode(registrationRequest.password()));
            CreateAuthentication(user);
            repository.save(user);
            return "account Successfully created";
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
    public AuthResponseDto AuthenticateUser(Authentication authentication, HttpServletResponse response) {
        ApplicationUser user = repository.findByUsername(authentication.getName())
                .orElseThrow(()->
                        new ResourceNotFoundException("USER NOT FOUND!")
                );
        String accessToken = tokenGenerator.GenerateAccessToken(authentication);
        String refreshToken = tokenGenerator.GenerateRefreshToken(authentication);
        RegisterRefreshToken(user,refreshToken);
        WriteAccessTokenToCookies(response,refreshToken);
        return new AuthResponseDto(accessToken,
                15*60,
                TokenType.Bearer,
                user.getUsername()
        );
    }

    public AuthResponseDto GetAccessTokenByRefreshToken(String authorizationHeader){
        if(!authorizationHeader.startsWith(TokenType.Bearer.name()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"authorization header not of type bearer");
        }
        String refreshToken = authorizationHeader.substring(7);

        JWTRefreshToken token = tokenRepository.findByRefreshToken(refreshToken)
                .filter(t->!t.getRevoked())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED,"refresh token is either revoked or doesn't exist")
                );

        ApplicationUser user = token.getOwner();
        Authentication authentication = CreateAuthentication(user);
        String accessToken = tokenGenerator.GenerateAccessToken(authentication);
        return new AuthResponseDto(
                accessToken,
                15*60,
                TokenType.Bearer,
                user.getUsername()
        );
    }

    public void RevokeRefreshToken(String authorizationHeader) {
        if(!authorizationHeader.startsWith(TokenType.Bearer.name()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"authorization header not of type bearer");
        String refreshToken = authorizationHeader.substring(7);
        JWTRefreshToken token = tokenRepository.findByRefreshToken(refreshToken)
                .filter(t->!t.getRevoked())
                .orElseThrow(()->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED,"refresh token is either revoked or doesn't exist")
                );
        token.setRevoked(true);
        tokenRepository.save(token);
        //I think I should now delete all user-owned tokens,but it may be worth it keep them for creating a log of every login attempt
    }
}
