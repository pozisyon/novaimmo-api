package com.novaimmo.demo.admin.dto;

import java.time.LocalDateTime;

public record AdminUserResponse(

        Long id,

        Long roleId,

        String roleCode,

        String roleNom,

        String nom,

        String prenom,

        String email,

        String telephone,

        Boolean actif,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}