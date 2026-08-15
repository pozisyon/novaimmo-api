package com.novaimmo.demo.transactiondocument;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionDocumentRepository
        extends JpaRepository<TransactionDocument, Long> {

    List<TransactionDocument>
    findByTransactionIdOrderByCreatedAtDesc(
            Long transactionId
    );
    List<TransactionDocument>
    findByTransactionClientIdOrderByCreatedAtDesc(
            Long clientId
    );
}