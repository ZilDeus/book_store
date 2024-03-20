package com.zildeus.application.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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

    @Column(name = "creation_date",nullable = false)
    private Timestamp creation;

    @Column(name = "deletion_date")
    private Timestamp deletion;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type",nullable = false)
    private UserType type;
}
