package com.zildeus.book_store.controller;

import com.zildeus.book_store.dto.BalanceDto;
import com.zildeus.book_store.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/secure/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;
    @PostMapping("balance")
    @PreAuthorize("hasAuthority('SCOPE_ADD_BALANCE')")
    ResponseEntity<?> AddBalanceToUser(Authentication authentication,@RequestBody BalanceDto balanceDto){
        System.out.println(authentication.getAuthorities());
        service.AddBalance(balanceDto);
        return ResponseEntity.ok("balance added successfully");
    }
}
