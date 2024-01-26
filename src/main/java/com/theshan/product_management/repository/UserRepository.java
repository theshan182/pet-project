package com.theshan.product_management.repository;

import com.theshan.product_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query(
            value = "SELECT * FROM users WHERE is_enabled=:value",
            nativeQuery = true
    )
    List<User> findByIsEnabled(@Param("value") boolean value);

}
