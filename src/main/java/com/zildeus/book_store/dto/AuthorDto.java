package com.zildeus.book_store.dto;

import com.zildeus.book_store.model.Author;
import com.zildeus.book_store.model.Book;

import java.sql.Timestamp;
import java.util.List;

public record AuthorDto(
        String name,
        Integer birthYear,
        String location,
        List<String> books
) {
    static public AuthorDto of(Author author){
        return new AuthorDto(
                author.getName(),
                author.getBirthYear(),
                author.getLocation(),
                author.getBooks().stream().map(Book::getTitle).toList()
        );
    }
}
