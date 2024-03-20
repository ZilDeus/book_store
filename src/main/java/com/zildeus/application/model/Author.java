package com.zildeus.application.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name",nullable = false,unique = true)
    private String name;

    @Column(name = "year_of_birth")
    private Integer year;

    @Column(name = "location")
    private String  location;
}
