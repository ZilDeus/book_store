package com.zildeus.book_store.repository;
import  com.zildeus.book_store.model.Book;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepositroy extends JpaRepository<Book,Long>{
    public Optional<Book> findByTitle(String title);
}
