package com.novaimmo.demo.visit.dto;


import java.time.LocalDateTime;

public record PropertyVisitResponse(

        Long id,

        Long propertyId,

        String propertyReference,

        String propertyTitle,

        Long clientId,

        Long agentId,

        String nomVisiteur,

        String email,

        String telephone,

        LocalDateTime dateVisite,

        Integer nombrePersonnes,

        String statut,

        String commentaire,

        LocalDateTime createdAt

) {
}