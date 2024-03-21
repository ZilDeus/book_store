package com.zildeus.book_store.service;

import com.zildeus.book_store.dto.ReviewDto;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
import com.zildeus.book_store.model.ApplicationUser;
import com.zildeus.book_store.model.Author;
import com.zildeus.book_store.model.Book;
import com.zildeus.book_store.model.Review;
import com.zildeus.book_store.repository.ApplicationUserRepository;
import com.zildeus.book_store.repository.BookRepositroy;
import com.zildeus.book_store.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;
    private final BookService bookService;

    public List<ReviewDto> GetReviews(){
        return repository.findAll()
                .stream().map(ReviewDto::of)
                .toList();
    }

    public List<ReviewDto> GetBookReviews(String bookTitle){
        return bookService.GetBookObject(bookTitle).getReviews()
                .stream().map(ReviewDto::of)
                .toList();
    }
    public void PostReview(ReviewDto r,String bookTitle)
    {
        Book book = bookService.GetBookObject(bookTitle);
        Review review = new Review();
        review.setRating(r.rating());
        review.setBook(book);
        review.setReview(r.review());
    }
}
