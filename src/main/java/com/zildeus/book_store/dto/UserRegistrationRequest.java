package com.zildeus.book_store.dto;

import java.util.List;

public record UserRegistrationRequest(
        String email,
        String username,
        String password,
        List<String> userRoles
) {
}
