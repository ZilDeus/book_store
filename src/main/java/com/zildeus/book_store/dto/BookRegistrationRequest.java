package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record BookRegistrationRequest(
        String title,
        String genre,
        Integer published_at,
        Float price
) {
}
