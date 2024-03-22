package com.zildeus.book_store.dto;

import java.sql.Timestamp;

public record ReportDto(
        BookDto book,
        String description,
        Timestamp posted_at,
        UserDto user
) {
}
