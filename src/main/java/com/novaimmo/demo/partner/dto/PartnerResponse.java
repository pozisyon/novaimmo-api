package com.novaimmo.demo.partner.dto;

import java.time.LocalDateTime;

public record PartnerResponse(

        Long id,

        String nom,

        String entreprise,

        String email,

        String telephone,

        String typePartenaire,

        Boolean actif,

        LocalDateTime createdAt

) {
}