package com.novaimmo.demo.contact.dto;


import java.time.LocalDateTime;

public record ContactResponse(

        Long id,

        String nom,

        String email,

        String telephone,

        String sujet,

        String message,

        String statut,

        LocalDateTime createdAt

) {
}
