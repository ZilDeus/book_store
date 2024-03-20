package com.zildeus.book_store.service;

import com.zildeus.book_store.model.ApplicationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ApplicationUser repository;
}
