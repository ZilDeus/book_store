package com.zildeus.book_store.mapper;

import com.zildeus.book_store.dto.*;
import com.zildeus.book_store.model.*;
import com.zildeus.book_store.repository.BookRepositroy;
import com.zildeus.book_store.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityMapper implements Mapper {
    private final BookRepositroy bookRepositroy;
    private final ReviewRepository reviewRepository;
    @Override
    public BookDto BookDtoFromBook(Book book) {
        return new BookDto(book.getId(),book.getTitle(), book.getGenre(),
                book.getPublishYear(), book.getUploadDate()
                ,book.getAuthor().getName(),
                book.getPrice(),
                reviewRepository.GetAverageBookRating(book.getId())
        );
    }

    @Override
    public Book BookFromBookDto(BookDto bookDto) {
        Author author = new Author();
        author.setName(bookDto.author());
        return new Book(
                null, bookDto.title(),
                bookDto.price(),bookDto.genre(),
                bookDto.published_at(),null,
                null,null
                ,author);
    }

    @Override
    public UserDto UserDtoFromUser(ApplicationUser user) {
        return new UserDto(
                user.getUsername(), user.getEmail(),
                user.getBalance(),user.getRoles(),
                user.getBooks().stream().map(book->BookDtoFromBook(book.getBook())).toList(),
                user.getReviews().stream().map(this::ReviewDtoFromReview).toList()
                );
    }

    @Override
    public AuthorDto AuthorDtoFromAuthor(Author author) {
        Author a = new Author();
        return new AuthorDto(
                author.getName(), author.getBirthYear(),
                author.getLocation(), author.getBooks().stream().map(this::BookDtoFromBook).toList()
        );
    }

    @Override
    public Author AuthorFromAuthorDto(AuthorRegistrationRequest authorDto) {
        return new Author(null, authorDto.name(), authorDto.birthYear(), authorDto.location(), null);
    }

    @Override
    public ReviewDto ReviewDtoFromReview(Review review) {
        return new ReviewDto(
                review.getReviewer().getUsername(),review.getRating(),
                review.getReview(),
                review.getPostedDate(),BookDtoFromBook(review.getBook()));
    }

    @Override
    public Review ReviewFromReviewDto(ReviewRegistrationRequest reviewDto) {
        return new Review(null, reviewDto.review(), reviewDto.rating(), null,null,null);
    }

    @Override
    public ReportDto ReportDtoFromReport(Report report) {
        return new ReportDto(BookDtoFromBook(report.getBook()),report.getDesc(),
                report.getIssuedAt(),UserDtoFromUser(report.getUser()));
    }

    @Override
    public Report ReportFromReportDto(ReportRegistrationRequest reportDto) {
        return new Report(null,reportDto.description(),
                null,false,null,null);
    }
}
