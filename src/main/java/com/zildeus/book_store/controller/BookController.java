package com.zildeus.book_store.controller;

import com.zildeus.book_store.dto.BookDto;
import com.zildeus.book_store.dto.ReviewDto;
import com.zildeus.book_store.model.Book;
import com.zildeus.book_store.model.Review;
import com.zildeus.book_store.service.AuthorService;
import com.zildeus.book_store.service.BookService;
import com.zildeus.book_store.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService service;
    private final ReviewService reviewService;

    @GetMapping()
    public List<BookDto> GetAllBooks(){
        return service.GetBooks();
    }
    @GetMapping("/{title}")
    public BookDto GetBook(@PathVariable String title){
        return service.GetBook(title);
    }
    @GetMapping("/{title}/review")
    public List<ReviewDto> GetBookReviews(@PathVariable String title){
        return reviewService.GetBookReviews(title);
    }
}
