package com.zildeus.book_store.repository;

import com.zildeus.book_store.model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser,Long> {
    public Optional<ApplicationUser> findByUsername(String username);
}
