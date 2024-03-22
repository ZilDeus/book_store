package com.zildeus.book_store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dashboard")
public class DashBoardController {
    @PreAuthorize("HasAuthority('SCOPE_READ')")
    @GetMapping("hi")
    ResponseEntity<?> HiApi(){
        return ResponseEntity.ok("AUTHORIZED");
    }
}
