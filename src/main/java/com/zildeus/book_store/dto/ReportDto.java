package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record ReportDto(
        String bookTitle,
        String description,
        Timestamp posted_at
) {
}
