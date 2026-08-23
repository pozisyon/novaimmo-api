package com.novaimmo.demo.admin.dto;

public record CreateAgentRequest(

        String nom,

        String prenom,

        String email,

        String password,

        String telephone

) {
}