package com.novaimmo.demo.partner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerRepository
        extends JpaRepository<Partner, Long> {

    List<Partner>
    findByActifTrueOrderByNomAsc();
}