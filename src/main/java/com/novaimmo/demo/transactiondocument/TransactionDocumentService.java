package com.novaimmo.demo.transactiondocument;

import com.novaimmo.demo.auth.CurrentUserService;
import com.novaimmo.demo.transaction.Transaction;
import com.novaimmo.demo.transaction.TransactionRepository;

import com.novaimmo.demo.transactiondocument.dto.CreateTransactionDocumentRequest;
import com.novaimmo.demo.transactiondocument.dto.TransactionDocumentResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class TransactionDocumentService {


    private static final Set<String> ALLOWED_TYPES =
            Set.of(
                    "CONTRAT",
                    "PROMESSE_VENTE",
                    "RECU",
                    "FACTURE",
                    "PIECE_IDENTITE",
                    "TITRE_PROPRIETE",
                    "AUTRE"
            );


    private final TransactionDocumentRepository documentRepository;
    private final TransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;

    public TransactionDocumentService(
            TransactionDocumentRepository documentRepository,
            TransactionRepository transactionRepository,
            CurrentUserService currentUserService
    ) {

        this.documentRepository = documentRepository;
        this.transactionRepository = transactionRepository;
        this.currentUserService=currentUserService;
    }


    @Transactional
    public TransactionDocumentResponse create(
            Long transactionId,
            CreateTransactionDocumentRequest request
    ) {

        Transaction transaction =
                findTransaction(transactionId);


        if ("ANNULEE".equals(transaction.getStatut())) {

            throw new RuntimeException(
                    "Impossible d'ajouter un document à une transaction annulée"
            );
        }


        String type =
                request.typeDocument()
                        .trim()
                        .toUpperCase();


        if (!ALLOWED_TYPES.contains(type)) {

            throw new RuntimeException(
                    "Type de document invalide"
            );
        }


        TransactionDocument document =
                new TransactionDocument();

        document.setTransaction(
                transaction
        );

        document.setTypeDocument(
                type
        );

        document.setNomFichier(
                request.nomFichier()
        );

        document.setFichierUrl(
                request.fichierUrl()
        );


        return toResponse(
                documentRepository.save(document)
        );
    }


    public List<TransactionDocumentResponse> findByTransaction(
            Long transactionId
    ) {

        findTransaction(transactionId);


        return documentRepository
                .findByTransactionIdOrderByCreatedAtDesc(
                        transactionId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TransactionDocumentResponse> findMyDocuments() {

        Long clientId =
                currentUserService.getCurrentUserId();

        return documentRepository
                .findByTransactionClientIdOrderByCreatedAtDesc(
                        clientId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public TransactionDocumentResponse findById(
            Long id
    ) {

        return toResponse(
                findEntity(id)
        );
    }


    @Transactional
    public void delete(
            Long id
    ) {

        TransactionDocument document =
                findEntity(id);

        documentRepository.delete(document);
    }


    private Transaction findTransaction(
            Long transactionId
    ) {

        return transactionRepository
                .findById(transactionId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Transaction introuvable : "
                                        + transactionId
                        )
                );
    }


    private TransactionDocument findEntity(
            Long id
    ) {

        return documentRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Document introuvable : " + id
                        )
                );
    }


    private TransactionDocumentResponse toResponse(
            TransactionDocument document
    ) {

        return new TransactionDocumentResponse(

                document.getId(),

                document.getTransaction()
                        .getId(),

                document.getTransaction()
                        .getReference(),

                document.getTypeDocument(),

                document.getNomFichier(),

                document.getFichierUrl(),

                document.getCreatedAt()
        );
    }
}