package com.zildeus.book_store.service;

import com.zildeus.book_store.dto.BookRegistrationRequest;
import com.zildeus.book_store.exceptions.DuplicateResourceException;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
import com.zildeus.book_store.model.Author;
import com.zildeus.book_store.model.Book;
import com.zildeus.book_store.repository.BookRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepositroy repositroy;
    private final AuthorService authorService;
    public List<Book> GetBooks(){
        return repositroy.findAll();
    }
    public Book GetBook(String title){
        return repositroy.findByTitle(title)
                .orElseThrow(()->
                        new ResourceNotFoundException("book with the title %s does not exist!".formatted(title))
                );
    }

    public  void AddBook(BookRegistrationRequest registrationRequest, String authorName){
        if(repositroy.findByTitle(registrationRequest.title()).isPresent()){
            throw new DuplicateResourceException("book with title %s already exists".formatted(registrationRequest.title()));
        }
        Author author = authorService.GetAuthor(authorName);
        Book book = new Book();
        book.setTitle(registrationRequest.title());
        book.setGenre(registrationRequest.genre());
        book.setPublishDate(registrationRequest.published_at());
        book.setAuthor(author);
        book.setPrice(registrationRequest.price());
        repositroy.save(book);
    }
}
