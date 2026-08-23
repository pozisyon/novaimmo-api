package com.novaimmo.demo.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByReference(
            String reference
    );

    List<Transaction>
    findByClientIdOrderByCreatedAtDesc(
            Long clientId
    );

    List<Transaction>
    findByAgentIdOrderByCreatedAtDesc(
            Long agentId
    );

    List<Transaction>
    findByPropertyIdOrderByCreatedAtDesc(
            Long propertyId
    );

    List<Transaction>
    findByStatutOrderByCreatedAtDesc(
            String statut
    );

    boolean existsByReference(
            String reference
    );
    long countByAgentId(
            Long agentId
    );

    long countByAgentIdAndStatut(
            Long agentId,
            String statut
    );
    Optional<Transaction> findByIdAndAgentId(
            Long id,
            Long agentId
    );

    long countByStatut(String statut);
}