package com.zildeus.book_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class ApplicationUser implements UserDetails {
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

    @Column(name = "deletion_date")
    private Timestamp deletion;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type",nullable = false)
    private List<UserRole> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles()
                .stream()
                .map(role->new SimpleGrantedAuthority(role.name()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
