package com.novaimmo.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(
            String email
    );

    boolean existsByEmail(
            String email
    );

    long countByRole_Code(
            String roleCode
    );
    List<User> findByRole_CodeOrderByNomAsc(
            String roleCode
    );
}