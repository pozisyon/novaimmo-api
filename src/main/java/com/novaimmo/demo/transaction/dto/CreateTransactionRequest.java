package com.novaimmo.demo.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTransactionRequest(

        @NotNull
        Long propertyId,

        @NotNull
        Long clientId,

        @NotBlank
        String typeTransaction,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal montant,

        String devise,

        String notes

) {
}