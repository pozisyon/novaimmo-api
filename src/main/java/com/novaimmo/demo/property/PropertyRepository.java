package com.novaimmo.demo.property;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    public interface PropertyRepository
            extends JpaRepository<Property, Long> {

        List<Property> findByStatut(String statut);

        List<Property> findByFeaturedTrue();

        List<Property> findByVilleIgnoreCase(String ville);

        List<Property> findByTransactionType(String transactionType);

        long countByStatut(String statut);
        boolean existsByReference(String reference);
}
