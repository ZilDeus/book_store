package com.zildeus.book_store.dto;

import com.zildeus.book_store.model.UserRole;

import java.util.List;

public record UserDto(
        String username,
        String email,
        Float balance,
        List<UserRole> userRoles,
        List<BookDto> books,
        List<ReviewDto> reviews
) {
}
