package com.novaimmo.demo.visit;


import com.novaimmo.demo.property.Property;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "property_visits")
@Getter
@Setter
public class PropertyVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;


    @Column(name = "client_id")
    private Long clientId;


    @Column(name = "agent_id")
    private Long agentId;


    @Column(name = "nom_visiteur", nullable = false, length = 150)
    private String nomVisiteur;


    @Column(length = 150)
    private String email;


    @Column(length = 30)
    private String telephone;


    @Column(name = "date_visite", nullable = false)
    private LocalDateTime dateVisite;


    @Column(name = "nombre_personnes", nullable = false)
    private Integer nombrePersonnes = 1;


    @Column(nullable = false, length = 30)
    private String statut = "DEMANDEE";


    @Column(columnDefinition = "TEXT")
    private String commentaire;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (statut == null) {
            statut = "DEMANDEE";
        }

        if (nombrePersonnes == null) {
            nombrePersonnes = 1;
        }
    }
}