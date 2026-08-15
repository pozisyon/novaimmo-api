package com.novaimmo.demo.transactiondocument.dto;

import java.time.LocalDateTime;

public record TransactionDocumentResponse(

        Long id,

        Long transactionId,

        String transactionReference,

        String typeDocument,

        String nomFichier,

        String fichierUrl,

        LocalDateTime createdAt

) {
}