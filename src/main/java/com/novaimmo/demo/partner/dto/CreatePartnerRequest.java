package com.novaimmo.demo.partner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreatePartnerRequest(

        @NotBlank
        String nom,

        String entreprise,

        @Email
        String email,

        String telephone,

        @NotBlank
        String typePartenaire

) {
}