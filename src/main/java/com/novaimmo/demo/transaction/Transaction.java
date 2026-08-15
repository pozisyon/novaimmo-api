package com.novaimmo.demo.transaction;

import com.novaimmo.demo.property.Property;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 50)
    private String reference;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;


    @Column(name = "client_id")
    private Long clientId;


    @Column(name = "agent_id")
    private Long agentId;


    @Column(
            name = "type_transaction",
            nullable = false,
            length = 30
    )
    private String typeTransaction;


    @Column(
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal montant;


    @Column(nullable = false, length = 10)
    private String devise = "USD";


    @Column(nullable = false, length = 30)
    private String statut = "EN_NEGOCIATION";


    @Column(name = "date_transaction")
    private LocalDateTime dateTransaction;


    @Column(columnDefinition = "TEXT")
    private String notes;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {

        LocalDateTime now =
                LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (devise == null) {
            devise = "USD";
        }

        if (statut == null) {
            statut = "EN_NEGOCIATION";
        }
    }


    @PreUpdate
    public void preUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}