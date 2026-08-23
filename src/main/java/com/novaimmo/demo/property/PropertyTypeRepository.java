package com.novaimmo.demo.property;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropertyTypeRepository
        extends JpaRepository<PropertyType, Long> {

    Optional<PropertyType> findByCode(String code);

    List<PropertyType> findAllByOrderByNomAsc();
}