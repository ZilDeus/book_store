package com.zildeus.book_store.service;

import com.zildeus.book_store.dto.UserDto;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
import com.zildeus.book_store.mapper.Mapper;
import com.zildeus.book_store.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ApplicationUserRepository repository;
    private final Mapper mapper;
    public List<UserDto> GetAllUsers(){
        return repository.findAll().stream().map(mapper::UserDtoFromUser).toList();
    }

    public UserDto GetUser(String username) {
        return mapper.UserDtoFromUser(
                repository.findByUsername(username).orElseThrow(
                ()->new ResourceNotFoundException("user with a username of %s not found".formatted(username))
        ));
    }
}
