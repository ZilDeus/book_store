package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record BookDto(
        String title,
        String genre,
        Integer published_at,
        Timestamp posted_at,
        String author,
        Float rating,
        Float price
) {
}
