package com.zildeus.book_store.config.application;

import com.zildeus.book_store.config.user.UserRole;
import com.zildeus.book_store.model.*;
import com.zildeus.book_store.repository.ApplicationUserRepository;
import com.zildeus.book_store.repository.AuthorRepository;
import com.zildeus.book_store.repository.BookRepositroy;
import com.zildeus.book_store.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreInitializer implements CommandLineRunner {
    Boolean start = true;
    private final AuthorRepository authorRepository;
    private final BookRepositroy bookRepositroy;
    private final ReviewRepository reviewRepository;
    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder encoder;
    public record FakeAuthors(String name, Integer birthYear, String location){ }
    public record FakeUser(String name, String password, String email, List<UserRole> roles){ }
    public record FakeBook(String title, String genre, Integer publishYear, Float price, Integer authorId){}
    public record FakeReview(String review, Integer rating,Integer bookId,Integer reviewer){}
    @Override
    //TODO: ENTER DUMB DATA
    public void run(String... args) throws Exception {
        if(!start)
            return;
        List<Author> authors = GenerateFakeAuthors(40).stream().map(a->{
            Author author = new Author();
            author.setName(a.name);
            author.setLocation(a.location);
            author.setBirthYear(a.birthYear);
            return author;
        }).toList();
        List<Book> books = GenerateFakeBooks(70).stream().map(b->{
            Book book = new Book();
            book.setPublishYear(b.publishYear);
            book.setPrice(b.price);
            book.setTitle(b.title);
            book.setGenre(b.genre);
            book.setAuthor(authors.get(b.authorId%authors.size()));
            return book;
        }).toList();
        List<ApplicationUser> users = GenerateFakeUsers(50).stream().map(u->{
            ApplicationUser user = new ApplicationUser();
            user.setUsername(u.name());
            user.setPassword(encoder.encode(u.password()));
            user.setEmail(u.email());
            user.setRoles(u.roles());
            user.setBalance(0.0f);
            return user;
        }).toList();
        List<Review> reviews =  GenerateFakeReviews(180).stream().map(r-> {
            Review review = new Review();
            review.setReview(r.review);
            review.setRating(r.rating);
            review.setBook(books.get(r.bookId%books.size()));
            review.setReviewer(users.get(r.reviewer%users.size()));
            return review;
        }).toList();
        authorRepository.saveAllAndFlush(authors);
        bookRepositroy.saveAllAndFlush(books);
        userRepository.saveAllAndFlush(users);
        reviewRepository.saveAllAndFlush(reviews);
    }
    public List<FakeUser> GenerateFakeUsers(int limit) throws Exception {
        List<FakeUser> users = new ArrayList<>();
        Faker faker = new Faker();
        while (--limit>=0){
            users.add(
                    new FakeUser(
                            faker.text().text(4,8),
                            faker.internet().password(4,8),
                            faker.internet().emailAddress(),
                            faker.number().numberBetween(0,10)==1?List.of(UserRole.Moderator,UserRole.Reader):List.of(UserRole.Reader)
                    )
            );
        }
        return users;
    }
    public List<FakeReview> GenerateFakeReviews(int limit) throws Exception {
        List<FakeReview> reviews = new ArrayList<>();
        Faker faker = new Faker();
        while (--limit>=0){
            reviews.add(
                    new FakeReview(faker.text().text(0,30)
                            ,faker.number().numberBetween(0,10)
                            ,faker.number().positive(),
                            faker.number().positive()
                    )
            );
        }
        return reviews;
    }

    public List<FakeAuthors> GenerateFakeAuthors(int limit) throws Exception {
        List<FakeAuthors> authors = new ArrayList<>();
        Faker faker = new Faker();
        while (--limit>=0){
            authors.add(
                    new FakeAuthors(faker.book().author()
                            ,faker.number().numberBetween(1850,2000)
                            ,faker.address().city())
            );
        }
        return authors;
    }

    public List<FakeBook> GenerateFakeBooks(int limit){
        List<FakeBook> books = new ArrayList<>();
        Faker faker = new Faker();
        while (--limit >= 0) {
            var fakeBook = faker.book();
            books.add(
                    new FakeBook(
                            fakeBook.title()
                            ,fakeBook.genre()
                            ,faker.number().numberBetween(1800,2020)
                            ,Double.valueOf(faker.number().randomDouble(2,5,200)).floatValue()
                            ,faker.number().positive()
                    )
            );
        }
        return books;
    }
}
