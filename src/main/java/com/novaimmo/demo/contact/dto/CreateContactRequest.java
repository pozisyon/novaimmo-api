package com.novaimmo.demo.contact.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateContactRequest(

        @NotBlank
        @Size(max = 150)
        String nom,

        @Email
        @Size(max = 150)
        String email,

        @Size(max = 30)
        String telephone,

        @Size(max = 200)
        String sujet,

        @NotBlank
        String message

) {
}
