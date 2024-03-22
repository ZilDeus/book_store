package com.zildeus.book_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "JWT_refresh_token")
public class JWTRefreshToken {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "refresh_token",nullable = false,length = 10000)
    private String refreshToken;

    @Column(name = "revoked")
    private Boolean revoked;

    @ManyToOne
    @JoinColumn(name = "owner",referencedColumnName = "id")
    private ApplicationUser owner;
}