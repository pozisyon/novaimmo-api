package com.novaimmo.demo.partner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "partners")
@Getter
@Setter
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(length = 150)
    private String entreprise;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String telephone;

    @Column(
            name = "type_partenaire",
            length = 50
    )
    private String typePartenaire;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (actif == null) {
            actif = true;
        }
    }
}