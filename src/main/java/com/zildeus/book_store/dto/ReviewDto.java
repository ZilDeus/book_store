package com.zildeus.book_store.dto;

import com.zildeus.book_store.model.Review;

import java.sql.Timestamp;

public record ReviewDto(
        String reviewer,
        Integer rating,
        String review,
        Timestamp posted_at
) {
    static public ReviewDto of(Review r){
        return new ReviewDto(
                r.getReviewer().getUsername(),
                r.getRating(),
                r.getReview(),
                r.getPostedDate()
        );
    }
}
