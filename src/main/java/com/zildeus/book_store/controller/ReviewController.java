package com.zildeus.book_store.controller;

import com.zildeus.book_store.model.Review;
import com.zildeus.book_store.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;
    @GetMapping()
    public List<Review> GetAllReviews(){
        return service.GetReviews();
    }
}
