package com.zildeus.book_store.controller;

import com.zildeus.book_store.dto.UserDto;
import com.zildeus.book_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/secure/user")
@RequiredArgsConstructor
public class ApplicationUsersController {
    private final UserService service;
    @PreAuthorize("hasAuthority('SCOPE_VIEW')")
    @GetMapping
    List<UserDto> GetAllUsers(){
        return service.GetAllUsers();
    }
    @PreAuthorize("hasAuthority('SCOPE_VIEW')")
    @GetMapping("/{username}")
    UserDto GetUserByUsername(@PathVariable String username){
        return service.GetUser(username);
    }
}
