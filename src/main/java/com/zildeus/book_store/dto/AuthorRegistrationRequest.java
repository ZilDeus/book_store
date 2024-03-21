package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record AuthorRegistrationRequest(
        String name,
        Integer birthYear,
        String location
) {
}
