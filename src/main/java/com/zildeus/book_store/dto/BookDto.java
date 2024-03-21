package com.zildeus.book_store.dto;

import com.zildeus.book_store.model.Book;

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
    static public BookDto of(Book book,Float averageRating){
        return new BookDto(
                book.getTitle()
                ,book.getGenre()
                ,book.getPublishYear()
                ,book.getUploadDate()
                ,book.getAuthor().getName()
                ,averageRating
                ,book.getPrice()
        );
    }
}
