package com.zildeus.book_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  com.zildeus.book_store.model.Report;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long>{
}
