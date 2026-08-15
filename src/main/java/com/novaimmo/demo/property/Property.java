package com.novaimmo.demo.property;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

    @Entity
    @Table(name = "properties")
    @Getter
    @Setter
    public class Property {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @Column(nullable = false, unique = true)
        private String reference;


        @ManyToOne
        @JoinColumn(
                name = "type_id",
                nullable = false
        )
        private PropertyType type;


        @Column(nullable = false)
        private String titre;


        @Column(columnDefinition = "TEXT")
        private String description;


        @Column(name = "transaction_type")
        private String transactionType;


        private BigDecimal prix;


        private String devise;


        private String adresse;

        private String quartier;

        private String ville;

        private String departement;

        private String pays;


        private BigDecimal latitude;

        private BigDecimal longitude;


        private Integer chambres;


        @Column(name = "salles_bain")
        private Integer sallesBain;


        private BigDecimal superficie;


        private String statut;


        private Boolean featured;


        @Column(name = "created_at")
        private LocalDateTime createdAt;


        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

}
