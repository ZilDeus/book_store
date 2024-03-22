package com.zildeus.book_store.mapper;

import com.zildeus.book_store.dto.*;
import com.zildeus.book_store.model.*;

public interface IMapper {
    BookDto BookDtoFromBook(Book book);
    Book BookFromBookDto(BookDto bookDto);
    UserDto UserDtoFromUser(ApplicationUser user);
    AuthorDto AuthorDtoFromAuthor(Author author);
    Author AuthorFromAuthorDto(AuthorRegistrationRequest authorDto);
    ReviewDto ReviewDtoFromReview(Review review);
    Review ReviewFromReviewDto(ReviewRegistrationRequest reviewDto);
    ReportDto ReportDtoFromReport(Report report);
    Report ReportFromReportDto(ReportRegistrationRequest reportDto);
}
