package com.novaimmo.demo.property.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PropertyResponse(

        Long id,

        String reference,

        Long typeId,

        String typeCode,

        String typeNom,

        String titre,

        String description,

        String transactionType,

        BigDecimal prix,

        String devise,

        String adresse,

        String quartier,

        String ville,

        String departement,

        String pays,

        BigDecimal latitude,

        BigDecimal longitude,

        Integer chambres,

        Integer sallesBain,

        BigDecimal superficie,

        String statut,

        Boolean featured,

        String mainImageUrl,

        List<PropertyImageResponse> images,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}