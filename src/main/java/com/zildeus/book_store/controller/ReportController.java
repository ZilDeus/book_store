package com.zildeus.book_store.controller;

import com.zildeus.book_store.model.Report;
import com.zildeus.book_store.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reports")
public class ReportController {
    private final ReportService service;
    @GetMapping()
    public List<Report> GetReports(){
        return service.GetReports();
    }
    @PostMapping("/{id}")
    public ResponseEntity<?> CloseReport(@PathVariable Long id){
        service.closeReport(id);
        return ResponseEntity.ok("report %d closed successfully".formatted(id));
    }
}
