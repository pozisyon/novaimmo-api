package com.novaimmo.demo.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(

        Long id,

        String reference,

        Long propertyId,

        String propertyReference,

        String propertyTitle,

        Long clientId,

        Long agentId,

        String typeTransaction,

        BigDecimal montant,

        String devise,

        String statut,

        LocalDateTime dateTransaction,

        String notes,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}