package com.novaimmo.demo.payment;

import com.novaimmo.demo.transaction.Transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(length = 100, unique = true)
    private String reference;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false, length = 10)
    private String devise = "USD";

    @Column(name = "mode_paiement", length = 50)
    private String modePaiement;

    @Column(nullable = false, length = 30)
    private String statut = "EN_ATTENTE";

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (devise == null) {
            devise = "USD";
        }

        if (statut == null) {
            statut = "EN_ATTENTE";
        }
    }
}