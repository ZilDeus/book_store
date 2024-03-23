package com.zildeus.book_store.dto;

import com.zildeus.book_store.model.UserRole;

import java.util.List;

public record UserDto(
        String username,
        String email,
        List<UserRole> userRoles,
        Float balance
) {
}
