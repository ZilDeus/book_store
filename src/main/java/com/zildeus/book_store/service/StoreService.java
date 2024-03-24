package com.zildeus.book_store.service;

import com.zildeus.book_store.exceptions.BadRequestException;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
import com.zildeus.book_store.model.ApplicationUser;
import com.zildeus.book_store.model.Book;
import com.zildeus.book_store.model.UserBooks;
import com.zildeus.book_store.repository.ApplicationUserRepository;
import com.zildeus.book_store.repository.BookRepositroy;
import com.zildeus.book_store.repository.UserBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final ApplicationUserRepository userRepository;
    private final BookRepositroy bookRepositroy;
    private final UserBooksRepository userBooksRepository;
    public void OrderBook(String username,Long bookId){
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("user not found")
                );
        Book book = bookRepositroy.findById(bookId)
                .orElseThrow(()-> new ResourceNotFoundException("book not found")
                );
        if(user.getBalance()<book.getPrice())
            throw  new BadRequestException("user doesn't have sufficient funds to buy this book");
        user.setBalance(user.getBalance()-book.getPrice());
        UserBooks userBook = new UserBooks(null,book,user);
        userBooksRepository.save(userBook);
    }
}
