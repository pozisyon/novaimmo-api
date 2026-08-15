package com.novaimmo.demo.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePaymentRequest(

        @NotNull
        @DecimalMin("0.01")
        BigDecimal montant,

        String devise,

        @NotBlank
        String modePaiement

) {
}