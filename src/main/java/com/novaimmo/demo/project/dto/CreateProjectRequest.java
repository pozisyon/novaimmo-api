package com.novaimmo.demo.project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record CreateProjectRequest(

        @NotBlank
        String nom,

        String description,

        String localisation,

        BigDecimal latitude,

        BigDecimal longitude,

        @DecimalMin("0.0")
        BigDecimal budget,

        String devise,

        String imageUrl

) {
}