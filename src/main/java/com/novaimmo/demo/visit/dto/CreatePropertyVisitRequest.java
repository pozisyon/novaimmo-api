package com.novaimmo.demo.visit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreatePropertyVisitRequest(

        @NotBlank
        String nomVisiteur,

        @Email
        String email,

        String telephone,

        @NotNull
        @Future
        LocalDateTime dateVisite,

        @Min(1)
        Integer nombrePersonnes,

        String commentaire

) {
}