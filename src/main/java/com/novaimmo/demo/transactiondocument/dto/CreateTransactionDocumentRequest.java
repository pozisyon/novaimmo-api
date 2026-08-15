package com.novaimmo.demo.transactiondocument.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTransactionDocumentRequest(

        @NotBlank
        String typeDocument,

        @NotBlank
        String nomFichier,

        @NotBlank
        String fichierUrl

) {
}