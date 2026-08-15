package com.novaimmo.demo.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProjectResponse(

        Long id,

        String reference,

        String nom,

        String description,

        String localisation,

        BigDecimal latitude,

        BigDecimal longitude,

        BigDecimal budget,

        String devise,

        String statut,

        String imageUrl,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}