package com.zildeus.book_store.service;

import com.zildeus.book_store.dto.ReportRegistrationRequest;
import com.zildeus.book_store.model.Book;
import com.zildeus.book_store.model.Report;
import com.zildeus.book_store.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository repository;
    private final BookService bookService;
    public List<Report> GetBookReports(String bookTitle){
        return bookService.GetBook(bookTitle).getReports();
    }
    public void  PostReport(ReportRegistrationRequest registrationRequest, String bookTitle){
        Book book = bookService.GetBook(bookTitle);
        Report report = new Report();
        report.setBook(book);
        report.setDesc(registrationRequest.description());
        repository.save(report);
    }
}
