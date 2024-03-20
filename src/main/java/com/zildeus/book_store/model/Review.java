package com.zildeus.book_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_review")
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "review",nullable = false)
    private String review;

    @Column(name = "rating",nullable = false)
    private Float rating;

    @Column(name = "date",columnDefinition = "timestamp(6) without time zone default NOW()")
    private Timestamp postedDate;

    @ManyToOne
    @JoinColumn(name = "book",referencedColumnName = "id")
    private Book book;
}
