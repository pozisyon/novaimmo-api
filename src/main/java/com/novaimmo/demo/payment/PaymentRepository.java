package com.novaimmo.demo.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment>
    findByTransactionIdOrderByCreatedAtAsc(
            Long transactionId
    );

    Optional<Payment> findByReference(
            String reference
    );
    List<Payment>
    findByTransactionClientIdOrderByCreatedAtDesc(
            Long clientId
    );

    List<Payment> findAllByOrderByCreatedAtDesc();

    @Query("""
    SELECT COALESCE(SUM(p.montant), 0)
    FROM Payment p
    WHERE p.statut = :statut
""")
    BigDecimal sumMontantByStatut(
            @Param("statut") String statut
    );
}