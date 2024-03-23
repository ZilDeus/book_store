package com.zildeus.book_store.controller;

import com.zildeus.book_store.dto.UserDto;
import com.zildeus.book_store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class ApplicationUsersController {
    private final UserService service;
    @GetMapping
    List<UserDto> GetAllUsers(){
        return service.GetAllUsers();
    }
}
