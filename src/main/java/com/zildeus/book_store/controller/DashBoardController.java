package com.zildeus.book_store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dashboard")
public class DashBoardController {
    @GetMapping("hi")
    ResponseEntity<?> HiApi(Authentication authentication){
        System.out.println(authentication.getAuthorities());
        System.out.println(authentication.getName());
        return ResponseEntity.ok("AUTHORIZED");
    }
}
