package com.zildeus.book_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "review")
    private String review;

    @Column(name = "rating",nullable = false)
    private Integer rating;

    @CreationTimestamp
    @Column(name = "date")
    private Timestamp postedDate;

    @ManyToOne
    @JoinColumn(name = "book",referencedColumnName = "id")
    private Book book;
    @ManyToOne
    @JoinColumn(name = "reviewer",referencedColumnName = "id")
    private ApplicationUser reviewer;
}
