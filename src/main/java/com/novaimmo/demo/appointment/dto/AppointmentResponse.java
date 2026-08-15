package com.novaimmo.demo.appointment.dto;


import java.time.LocalDateTime;

public record AppointmentResponse(

        Long id,
        Long clientId,
        Long agentId,
        String nomContact,
        String email,
        String telephone,
        String sujet,
        LocalDateTime dateDebut,
        LocalDateTime dateFin,
        String lieu,
        String statut,
        String notes,
        LocalDateTime createdAt
) {
}