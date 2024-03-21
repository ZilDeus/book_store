package com.zildeus.book_store.controller;

import com.zildeus.book_store.dto.AuthorDto;
import com.zildeus.book_store.model.Author;
import com.zildeus.book_store.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/authors")
public class AuthorController {
    private final AuthorService service;
    @GetMapping
    public List<AuthorDto> GetAuthors(){
        return service.GetAuthors();
    }

    @GetMapping("/{name}")
    public AuthorDto GetAuthor(@PathVariable String name){
        return service.GetAuthor(name);
    }
}
