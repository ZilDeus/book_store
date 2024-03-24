package com.zildeus.book_store.dto;


import com.zildeus.book_store.config.user.UserRole;

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
