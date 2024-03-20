package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record ReviewDto(
        Float rating,
        String review,
        Timestamp posted_at
) {
}
