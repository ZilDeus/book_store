package com.zildeus.book_store.repository;

import com.zildeus.book_store.model.UserBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBooksRepository extends JpaRepository<UserBooks,Long> {
}
