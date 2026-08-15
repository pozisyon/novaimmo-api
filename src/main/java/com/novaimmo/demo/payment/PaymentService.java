package com.novaimmo.demo.payment;

import com.novaimmo.demo.auth.CurrentUserService;
import com.novaimmo.demo.payment.dto.CreatePaymentRequest;
import com.novaimmo.demo.payment.dto.PaymentResponse;
import com.novaimmo.demo.payment.dto.TransactionPaymentSummary;

import com.novaimmo.demo.transaction.Transaction;
import com.novaimmo.demo.transaction.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final TransactionRepository transactionRepository;
    private final CurrentUserService currentUserService;

    public PaymentService(
            PaymentRepository paymentRepository,
            TransactionRepository transactionRepository,
            CurrentUserService currentUserService
    ) {

        this.paymentRepository =
                paymentRepository;

        this.transactionRepository =
                transactionRepository;

        this.currentUserService = currentUserService;
    }


    @Transactional
    public PaymentResponse create(
            Long transactionId,
            CreatePaymentRequest request
    ) {

        Transaction transaction =
                findTransaction(transactionId);


        if ("ANNULEE".equals(
                transaction.getStatut()
        )) {

            throw new RuntimeException(
                    "Impossible d'ajouter un paiement à une transaction annulée"
            );
        }


        if ("TERMINEE".equals(
                transaction.getStatut()
        )) {

            throw new RuntimeException(
                    "Cette transaction est déjà terminée"
            );
        }


        BigDecimal totalPaid =
                calculateTotalPaid(
                        transactionId
                );


        BigDecimal remaining =
                transaction.getMontant()
                        .subtract(totalPaid);


        if (request.montant()
                .compareTo(remaining) > 0) {

            throw new RuntimeException(
                    "Le paiement dépasse le solde restant de "
                            + remaining
                            + " "
                            + transaction.getDevise()
            );
        }


        Payment payment =
                new Payment();


        payment.setTransaction(
                transaction
        );


        payment.setReference(
                generateReference()
        );


        payment.setMontant(
                request.montant()
        );


        payment.setDevise(
                request.devise() == null
                        || request.devise().isBlank()
                        ? transaction.getDevise()
                        : request.devise().toUpperCase()
        );


        payment.setModePaiement(
                request.modePaiement()
                        .toUpperCase()
        );


        /*
         * Pour l'instant le paiement est créé
         * en attente de validation.
         */
        payment.setStatut(
                "EN_ATTENTE"
        );


        return toResponse(
                paymentRepository.save(payment)
        );
    }


    public List<PaymentResponse> findByTransaction(
            Long transactionId
    ) {

        findTransaction(transactionId);


        return paymentRepository
                .findByTransactionIdOrderByCreatedAtAsc(
                        transactionId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public PaymentResponse findById(
            Long id
    ) {

        return toResponse(
                findEntity(id)
        );
    }


    @Transactional
    public PaymentResponse confirm(
            Long id
    ) {

        Payment payment =
                findEntity(id);


        if (!"EN_ATTENTE".equals(
                payment.getStatut()
        )) {

            throw new RuntimeException(
                    "Seul un paiement en attente peut être confirmé"
            );
        }


        payment.setStatut(
                "PAYE"
        );

        payment.setDatePaiement(
                LocalDateTime.now()
        );


        return toResponse(
                paymentRepository.save(payment)
        );
    }


    @Transactional
    public PaymentResponse fail(
            Long id
    ) {

        Payment payment =
                findEntity(id);


        if (!"EN_ATTENTE".equals(
                payment.getStatut()
        )) {

            throw new RuntimeException(
                    "Ce paiement ne peut pas être marqué comme échoué"
            );
        }


        payment.setStatut(
                "ECHOUE"
        );


        return toResponse(
                paymentRepository.save(payment)
        );
    }


    @Transactional
    public PaymentResponse refund(
            Long id
    ) {

        Payment payment =
                findEntity(id);


        if (!"PAYE".equals(
                payment.getStatut()
        )) {

            throw new RuntimeException(
                    "Seul un paiement confirmé peut être remboursé"
            );
        }


        payment.setStatut(
                "REMBOURSE"
        );


        return toResponse(
                paymentRepository.save(payment)
        );
    }


    public TransactionPaymentSummary getSummary(
            Long transactionId
    ) {

        Transaction transaction =
                findTransaction(transactionId);


        BigDecimal totalPaid =
                calculateTotalPaid(
                        transactionId
                );


        BigDecimal remaining =
                transaction.getMontant()
                        .subtract(totalPaid);


        return new TransactionPaymentSummary(

                transaction.getId(),

                transaction.getReference(),

                transaction.getMontant(),

                totalPaid,

                remaining,

                transaction.getDevise(),

                remaining.compareTo(
                        BigDecimal.ZERO
                ) == 0
        );
    }


    public boolean isFullyPaid(
            Long transactionId
    ) {

        Transaction transaction =
                findTransaction(transactionId);


        BigDecimal totalPaid =
                calculateTotalPaid(
                        transactionId
                );


        return totalPaid.compareTo(
                transaction.getMontant()
        ) >= 0;
    }


    private BigDecimal calculateTotalPaid(
            Long transactionId
    ) {

        return paymentRepository
                .findByTransactionIdOrderByCreatedAtAsc(
                        transactionId
                )
                .stream()

                /*
                 * Seuls les paiements réellement confirmés
                 * sont comptabilisés.
                 */
                .filter(payment ->
                        "PAYE".equals(
                                payment.getStatut()
                        )
                )

                .map(Payment::getMontant)

                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
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


    private Payment findEntity(
            Long id
    ) {

        return paymentRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Paiement introuvable : " + id
                        )
                );
    }


    private String generateReference() {

        return "PAY-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();
    }


    private PaymentResponse toResponse(
            Payment payment
    ) {

        return new PaymentResponse(

                payment.getId(),

                payment.getTransaction()
                        .getId(),

                payment.getTransaction()
                        .getReference(),

                payment.getReference(),

                payment.getMontant(),

                payment.getDevise(),

                payment.getModePaiement(),

                payment.getStatut(),

                payment.getDatePaiement(),

                payment.getCreatedAt()
        );
    }
    public List<PaymentResponse> findMyPayments() {

        Long clientId =
                currentUserService.getCurrentUserId();

        return paymentRepository
                .findByTransactionClientIdOrderByCreatedAtDesc(clientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}