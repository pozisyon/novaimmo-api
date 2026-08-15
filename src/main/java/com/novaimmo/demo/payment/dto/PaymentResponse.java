package com.novaimmo.demo.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(

        Long id,

        Long transactionId,

        String transactionReference,

        String reference,

        BigDecimal montant,

        String devise,

        String modePaiement,

        String statut,

        LocalDateTime datePaiement,

        LocalDateTime createdAt

) {
}