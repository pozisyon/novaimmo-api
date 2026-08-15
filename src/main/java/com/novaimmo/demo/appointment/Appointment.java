package com.novaimmo.demo.appointment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "nom_contact", nullable = false, length = 150)
    private String nomContact;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String telephone;

    @Column(nullable = false, length = 200)
    private String sujet;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Column(length = 255)
    private String lieu;

    @Column(nullable = false, length = 30)
    private String statut = "DEMANDE";

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (statut == null) {
            statut = "DEMANDE";
        }
    }
}
