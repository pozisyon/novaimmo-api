package com.novaimmo.demo.payment.dto;

import java.math.BigDecimal;

public record TransactionPaymentSummary(

        Long transactionId,

        String transactionReference,

        BigDecimal montantTransaction,

        BigDecimal totalPaye,

        BigDecimal soldeRestant,

        String devise,

        boolean entierementPayee

) {
}