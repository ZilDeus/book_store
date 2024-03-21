package com.zildeus.book_store.dto;

import java.sql.Timestamp;
import java.util.List;

public record AuthorDto(
        String name,
        Integer birthYear,
        String location,
        List<String> books
) {
}
