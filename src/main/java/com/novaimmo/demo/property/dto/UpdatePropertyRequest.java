package com.novaimmo.demo.property.dto;

import java.math.BigDecimal;

public record UpdatePropertyRequest(

        Long typeId,

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

        Boolean featured

) {
}