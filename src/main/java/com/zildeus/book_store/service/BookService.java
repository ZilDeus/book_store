package com.zildeus.book_store.service;

import com.zildeus.book_store.dto.AuthorDto;
import com.zildeus.book_store.dto.BookDto;
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
    private final BookRepositroy repository;
    private final AuthorService authorService;
    public List<BookDto> GetBooks(){
        return repository.findAll()
                .stream().map(book ->
                    new BookDto(
                            book.getTitle()
                            ,book.getGenre()
                            , book.getPublishYear()
                            , book.getUploadDate()
                            ,book.getAuthor().getName()
                            ,book.getPrice()
                            ,0.0f
                    )
                ).toList();
    }
    public Book GetBookObject(String title){
        return repository.findByTitle(title)
                .orElseThrow(()->
                        new ResourceNotFoundException("book with the title %s does not exist!".formatted(title))
                );
    }
    public BookDto GetBook(String title){
        Book book = GetBookObject(title);
        return new BookDto(
                book.getTitle()
                ,book.getGenre()
                , book.getPublishYear()
                , book.getUploadDate()
                ,book.getAuthor().getName()
                ,book.getPrice()
                ,0.0f);
    }

    public  void AddBook(BookRegistrationRequest registrationRequest, String authorName){
        Author author = authorService.GetAuthorObject(authorName);
        if(author.getBooks().stream().anyMatch(b->b.getTitle().equals(registrationRequest.title()))){
            throw new DuplicateResourceException("book with title %s already exists".formatted(registrationRequest.title()));
        }
        Book book = new Book();
        book.setTitle(registrationRequest.title());
        book.setGenre(registrationRequest.genre());
        book.setPublishYear(registrationRequest.published_at());
        book.setAuthor(author);
        book.setPrice(registrationRequest.price());
        repository.save(book);
    }
}
