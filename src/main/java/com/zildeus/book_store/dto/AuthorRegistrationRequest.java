package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record AuthorRegistrationRequest(
        String name,
        Timestamp birthday,
        String location
) {
}
