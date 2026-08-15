package com.novaimmo.demo.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository
        extends JpaRepository<Project, Long> {

    Optional<Project> findByReference(String reference);

    List<Project> findByStatutOrderByCreatedAtDesc(
            String statut
    );
}