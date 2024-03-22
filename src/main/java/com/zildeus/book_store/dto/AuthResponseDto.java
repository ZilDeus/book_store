package com.zildeus.book_store.dto;

public record AuthResponseDto(
        String accessToken,
        int accessTokenExpiry,
        TokenType tokenType,
        String userName
) {
}