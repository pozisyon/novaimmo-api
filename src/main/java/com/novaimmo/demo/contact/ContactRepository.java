package com.novaimmo.demo.contact;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository
        extends JpaRepository<Contact, Long> {

    List<Contact> findByStatutOrderByCreatedAtDesc(
            String statut
    );
}