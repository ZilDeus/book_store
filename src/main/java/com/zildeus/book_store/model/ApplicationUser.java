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
@Table(name = "app_user")
public class ApplicationUser {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username",nullable = false,unique = true)
    private String username;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "balance",nullable = false)
    private Float balance;

    @CreationTimestamp
    private Timestamp creation;

    @Column(name = "deletion_date",nullable = false)
    private Timestamp deletion;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type",nullable = false)
    private UserType type;
}
