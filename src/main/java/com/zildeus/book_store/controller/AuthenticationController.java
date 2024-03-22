package com.zildeus.book_store.controller;

import com.zildeus.book_store.dto.UserRegistrationRequest;
import com.zildeus.book_store.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("sign-in")
    ResponseEntity<?> SignIn(Authentication authentication,HttpServletResponse response){
        return ResponseEntity.ok(authService.GetAccessTokenFromRefreshToken(authentication,response));
    }
    @PostMapping("refresh-token")
    ResponseEntity<?> SignInWithToken(Authentication authentication,HttpServletResponse response){
        return ResponseEntity.ok(authService.GetAccessTokenFromRefreshToken(authentication,response));
    }
    @PostMapping("sign-up")
    ResponseEntity<?> SignUp(@RequestBody UserRegistrationRequest registrationRequest,
                             BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(bindingResult.getAllErrors()
                            .stream().map(DefaultMessageSourceResolvable::new)
                            .toList());
        }
        return ResponseEntity.ok().body(authService.RegisterNewUser(registrationRequest,response));
    }
}
