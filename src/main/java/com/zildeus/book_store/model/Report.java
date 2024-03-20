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
@Table(name = "book_report")
public class Report {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "description",nullable = false)
    private String desc;

    @Column(name = "issued_at",columnDefinition = "timestamp(6) without time zone default NOW()")
    private Timestamp issuedAt;

    @Column(name = "read",columnDefinition = "boolean default false")
    private Boolean read;

    @ManyToOne
    @JoinColumn(name = "book",referencedColumnName = "id")
    private Book book;
}
