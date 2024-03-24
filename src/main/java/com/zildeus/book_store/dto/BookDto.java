package com.zildeus.book_store.dto;

import com.zildeus.book_store.model.Book;

import java.sql.Timestamp;

public record BookDto(
        Long id,
        String title,
        String genre,
        Integer published_at,
        Timestamp posted_at,
        String author,
        Float price,
        Float rating
) {
}
