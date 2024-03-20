package com.zildeus.book_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  com.zildeus.book_store.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long>{

}
