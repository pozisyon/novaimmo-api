package com.novaimmo.demo.auth.dto;

public record AuthResponse(

        String token,

        Long userId,

        String nom,

        String email,

        String role

) {
}