package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record AuthorDto(
        String name,
        Timestamp birthday,
        String location,
        Float rating
) {
}
