package com.zildeus.book_store.dto;

import com.zildeus.book_store.model.UserType;
import lombok.Builder;

public record UserDto(
        String username,
        String email,
        UserType userType,
        Float balance
) {
}
