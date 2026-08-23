package com.novaimmo.demo.transaction;

import com.novaimmo.demo.auth.CurrentUserService;

import com.novaimmo.demo.payment.PaymentService;
import com.novaimmo.demo.property.Property;
import com.novaimmo.demo.property.PropertyRepository;

import com.novaimmo.demo.transaction.dto.CreateTransactionRequest;
import com.novaimmo.demo.transaction.dto.TransactionResponse;

import com.novaimmo.demo.user.User;
import com.novaimmo.demo.user.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final PaymentService paymentService;

    private final TransactionRepository transactionRepository;

    private final PropertyRepository propertyRepository;

    private final UserRepository userRepository;

    private final CurrentUserService currentUserService;


    public TransactionService(
            TransactionRepository transactionRepository,
            PropertyRepository propertyRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService,
            PaymentService paymentService
    ) {

        this.transactionRepository = transactionRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.paymentService = paymentService;
    }


    /*
     * =====================================================
     * CREATION
     * =====================================================
     */
    @Transactional
    public TransactionResponse create(
            CreateTransactionRequest request
    ) {

        Property property =
                propertyRepository
                        .findById(request.propertyId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Propriété introuvable"
                                )
                        );


        /*
         * Vérification du client.
         */
        User client =
                userRepository
                        .findById(request.clientId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Client introuvable"
                                )
                        );


        if (!"CLIENT".equals(
                client.getRole().getCode()
        )) {

            throw new RuntimeException(
                    "L'utilisateur sélectionné n'est pas un client"
            );
        }


        /*
         * Seules les propriétés disponibles
         * peuvent démarrer une transaction.
         */
        if (!"DISPONIBLE".equals(
                property.getStatut()
        )) {

            throw new RuntimeException(
                    "Cette propriété n'est pas disponible"
            );
        }


        /*
         * Vérification du type.
         */
        String type =
                request.typeTransaction()
                        .toUpperCase();


        if (!type.equals("VENTE")
                && !type.equals("LOCATION")
                && !type.equals("INVESTISSEMENT")) {

            throw new RuntimeException(
                    "Type de transaction invalide"
            );
        }


        /*
         * Agent connecté.
         */
        User currentUser =
                currentUserService
                        .getCurrentUser();


        Transaction transaction =
                new Transaction();


        transaction.setReference(
                generateReference()
        );

        transaction.setProperty(
                property
        );

        transaction.setClientId(
                client.getId()
        );

        transaction.setAgentId(
                currentUser.getId()
        );

        transaction.setTypeTransaction(
                type
        );

        transaction.setMontant(
                request.montant()
        );

        transaction.setDevise(
                request.devise() == null
                        || request.devise().isBlank()
                        ? "USD"
                        : request.devise().toUpperCase()
        );

        transaction.setNotes(
                request.notes()
        );

        transaction.setStatut(
                "EN_NEGOCIATION"
        );


        return toResponse(
                transactionRepository
                        .save(transaction)
        );
    }


    /*
     * =====================================================
     * LISTE
     * =====================================================
     */
    public List<TransactionResponse> findAll() {

        return transactionRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    /*
     * =====================================================
     * TRANSACTION PAR ID
     * =====================================================
     */
    public TransactionResponse findById(
            Long id
    ) {

        return toResponse(
                findEntity(id)
        );
    }


    /*
     * =====================================================
     * MES TRANSACTIONS
     * =====================================================
     */
    public List<TransactionResponse> findMyTransactions() {

        Long clientId =
                currentUserService
                        .getCurrentUserId();


        return transactionRepository
                .findByClientIdOrderByCreatedAtDesc(
                        clientId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    /*
     * =====================================================
     * PASSER EN ATTENTE
     * =====================================================
     */
    @Transactional
    public TransactionResponse markPending(
            Long id
    ) {

        Transaction transaction =
                findEntity(id);


        if (!"EN_NEGOCIATION".equals(
                transaction.getStatut()
        )) {

            throw new RuntimeException(
                    "La transaction doit être en négociation"
            );
        }


        transaction.setStatut(
                "EN_ATTENTE"
        );


        return toResponse(
                transactionRepository
                        .save(transaction)
        );
    }


    /*
     * =====================================================
     * CONFIRMER
     * =====================================================
     */
    @Transactional
    public TransactionResponse confirm(
            Long id
    ) {

        Transaction transaction =
                findEntity(id);


        if (!"EN_ATTENTE".equals(
                transaction.getStatut()
        )) {

            throw new RuntimeException(
                    "La transaction doit être en attente"
            );
        }


        transaction.setStatut(
                "CONFIRMEE"
        );


        return toResponse(
                transactionRepository
                        .save(transaction)
        );
    }


    /*
     * =====================================================
     * TERMINER
     * =====================================================
     */
    @Transactional
    public TransactionResponse complete(
            Long id
    ) {

        Transaction transaction =
                findEntity(id);


        if (!"CONFIRMEE".equals(
                transaction.getStatut()
        )) {

            throw new RuntimeException(
                    "La transaction doit être confirmée"
            );
        }

        if (!paymentService.isFullyPaid(id)) {

            throw new RuntimeException(
                    "La transaction ne peut pas être terminée : le paiement n'est pas complet"
            );
        }
        transaction.setStatut(
                "TERMINEE"
        );

        transaction.setDateTransaction(
                LocalDateTime.now()
        );


        /*
         * Mise à jour automatique
         * de la propriété.
         */
        Property property =
                transaction.getProperty();


        if ("VENTE".equals(
                transaction.getTypeTransaction()
        )) {

            property.setStatut(
                    "VENDU"
            );

        } else if ("LOCATION".equals(
                transaction.getTypeTransaction()
        )) {

            property.setStatut(
                    "LOUE"
            );
        }


        propertyRepository.save(
                property
        );


        return toResponse(
                transactionRepository
                        .save(transaction)
        );
    }


    /*
     * =====================================================
     * ANNULATION
     * =====================================================
     */
    @Transactional
    public TransactionResponse cancel(
            Long id
    ) {

        Transaction transaction =
                findEntity(id);


        if ("TERMINEE".equals(
                transaction.getStatut()
        )) {

            throw new RuntimeException(
                    "Une transaction terminée ne peut pas être annulée"
            );
        }


        transaction.setStatut(
                "ANNULEE"
        );


        return toResponse(
                transactionRepository
                        .save(transaction)
        );
    }


    private Transaction findEntity(
            Long id
    ) {

        return transactionRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Transaction introuvable : " + id
                        )
                );
    }


    /*
     * Référence :
     *
     * TRX-A7F23910
     */
    private String generateReference() {

        return "TRX-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();
    }


    private TransactionResponse toResponse(
            Transaction transaction
    ) {

        return new TransactionResponse(

                transaction.getId(),

                transaction.getReference(),

                transaction.getProperty().getId(),

                transaction.getProperty().getReference(),

                transaction.getProperty().getTitre(),

                transaction.getClientId(),

                transaction.getAgentId(),

                transaction.getTypeTransaction(),

                transaction.getMontant(),

                transaction.getDevise(),

                transaction.getStatut(),

                transaction.getDateTransaction(),

                transaction.getNotes(),

                transaction.getCreatedAt(),

                transaction.getUpdatedAt()
        );
    }
    @Transactional
    public TransactionResponse assignAgent(
            Long transactionId,
            Long agentId
    ) {

        Transaction transaction =
                transactionRepository
                        .findById(transactionId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Transaction introuvable"
                                )
                        );

        transaction.setAgentId(agentId);

        return toResponse(
                transactionRepository.save(transaction)
        );
    }
}