package com.zildeus.book_store.service;

import com.zildeus.book_store.dto.ReportRegistrationRequest;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
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
        return bookService.GetBookObject(bookTitle).getReports();
    }
    public void  PostReport(ReportRegistrationRequest registrationRequest, String bookTitle){
        Book book = bookService.GetBookObject(bookTitle);
        Report report = new Report();
        report.setBook(book);
        report.setDesc(registrationRequest.description());
        repository.save(report);
    }

    public List<Report> GetReports() {
        return repository.findAll().stream().filter(r->!r.getRead()).toList();
    }
    public void closeReport(Long id) {
        Report report = repository.findById(id)
                .orElseThrow(
                        ()->new ResourceNotFoundException("report with id %d not found".formatted(id))
                );
        report.setRead(true);
        repository.save(report);
    }
}
