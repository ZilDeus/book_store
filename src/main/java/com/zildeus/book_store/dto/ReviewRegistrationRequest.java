package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record ReviewRegistrationRequest(
        Integer rating,
        String review
) {
}
