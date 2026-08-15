package com.novaimmo.demo.property;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

    @Entity
    @Table(name = "property_images")
    @Getter
    @Setter
    public class PropertyImage {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "property_id", nullable = false)
        private Property property;

        @Column(name = "image_url", nullable = false, length = 500)
        private String imageUrl;

        @Column(length = 150)
        private String titre;

        @Column(nullable = false)
        private Boolean principale = false;

        @Column(name = "ordre_affichage", nullable = false)
        private Integer ordreAffichage = 0;

        @Column(name = "created_at", nullable = false)
        private LocalDateTime createdAt;

        @PrePersist
        public void prePersist() {

            if (createdAt == null) {
                createdAt = LocalDateTime.now();
            }

            if (principale == null) {
                principale = false;
            }

            if (ordreAffichage == null) {
                ordreAffichage = 0;
            }
        }

}
