package com.zildeus.book_store.dto;

public record UserRegistrationRequest(
        String email,
        String username,
        String password,
        String userType
) {
}
