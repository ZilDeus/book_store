package com.zildeus.book_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "title",nullable = false,unique = true)
    private String title;

    @Column(name = "price",nullable = false)
    private Float price;

    @Column(name = "genre",nullable = false)
    private String genre;

    @Column(name = "publish_date",nullable = false)
    private Timestamp publishDate;

    @Column(name = "upload_date",columnDefinition = "timestamp(6) without time zone default NOW()")
    private Timestamp uploadDate;

    @OneToMany(mappedBy = "book")
    private List<Review> reviews;

    @OneToMany(mappedBy = "book")
    private List<Report> reports;

    @ManyToOne
    @JoinColumn(name = "author",referencedColumnName = "id")
    private Author author;
}
