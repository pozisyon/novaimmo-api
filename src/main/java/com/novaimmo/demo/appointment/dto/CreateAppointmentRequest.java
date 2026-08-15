package com.novaimmo.demo.appointment.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentRequest(

        @NotBlank
        String nomContact,

        @Email
        String email,

        String telephone,

        @NotBlank
        String sujet,

        @NotNull
        @Future
        LocalDateTime dateDebut,

        LocalDateTime dateFin,

        String lieu,

        String notes
) {
}