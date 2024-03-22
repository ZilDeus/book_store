package com.zildeus.book_store.repository;

import com.zildeus.book_store.model.JWTRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JWTRefreshTokenRepository extends JpaRepository<JWTRefreshToken,Long> {
    Optional<JWTRefreshToken> findByRefreshToken(String refreshToken);
}
