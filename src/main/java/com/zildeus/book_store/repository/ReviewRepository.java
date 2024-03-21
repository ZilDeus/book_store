package com.zildeus.book_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  com.zildeus.book_store.model.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ReviewRepository extends JpaRepository<Review,Long>{
    @Query( value = "SELECT AVG(rating) from book_review where book=?1",nativeQuery = true )
    public Float GetAverageBookRating(Long bookId);
}
