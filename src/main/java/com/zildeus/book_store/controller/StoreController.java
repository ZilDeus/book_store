package com.zildeus.book_store.controller;

import com.zildeus.book_store.service.StoreService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/secure/store")
public class StoreController {
    private final StoreService service;
    @PreAuthorize("hasAuthority('SCOPE_BUY')")
    @PostMapping("order")
    ResponseEntity<?> OrderBook(Authentication authentication,@PathParam("bookId") Long bookId)
    {
        service.OrderBook(authentication.getName(),bookId);
        return ResponseEntity.ok("book successfully added to user books");
    }
}
