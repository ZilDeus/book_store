package com.zildeus.book_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_books")
public class UserBooks {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book",referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "buyer",referencedColumnName = "id")
    private ApplicationUser buyer;
}
