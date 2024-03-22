package com.zildeus.book_store.dto;

import com.zildeus.book_store.model.Author;
import com.zildeus.book_store.model.Book;

import java.sql.Timestamp;
import java.util.List;

public record AuthorDto(
        String name,
        Integer birthYear,
        String location,
        List<BookDto> books
) {
}
