package com.zildeus.book_store.controller;

import com.zildeus.book_store.dto.UserRegistrationRequest;
import com.zildeus.book_store.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("sign-in")
    ResponseEntity<?> SignIn(Authentication authentication,HttpServletResponse response){
        return ResponseEntity.ok(authService.AuthenticateUser(authentication,response));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")
    @PostMapping("token")
    ResponseEntity<?> RefreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        return ResponseEntity.ok(authService.GetAccessTokenByRefreshToken(authorizationHeader));
    }

    @PostMapping("log-out")
    ResponseEntity<?> LogOut(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        authService.RevokeRefreshToken(authorizationHeader);
        return ResponseEntity.ok("successfully revoked refresh token");
    }
    @PostMapping("sign-up")
    ResponseEntity<?> SignUp(@RequestBody UserRegistrationRequest registrationRequest,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(bindingResult.getAllErrors()
                            .stream().map(DefaultMessageSourceResolvable::new)
                            .toList());
        }
        return ResponseEntity.ok().body(authService.RegisterNewUser(registrationRequest));
    }
}
