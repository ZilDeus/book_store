package com.zildeus.book_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "price",nullable = false)
    private Float price;

    @Column(name = "genre",nullable = false)
    private String genre;

    @Column(name = "publish_year",nullable = false)
    private Integer publishYear;

    @CreationTimestamp
    @Column(name = "upload_date")
    private Timestamp uploadDate;

    @OneToMany(mappedBy = "book")
    private List<Review> reviews;

    @OneToMany(mappedBy = "book")
    private List<Report> reports;

    @ManyToOne
    @JoinColumn(name = "author",referencedColumnName = "id")
    private Author author;
}
