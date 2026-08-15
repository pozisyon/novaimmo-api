package com.novaimmo.demo.payment;

import com.novaimmo.demo.payment.dto.CreatePaymentRequest;
import com.novaimmo.demo.payment.dto.PaymentResponse;
import com.novaimmo.demo.payment.dto.TransactionPaymentSummary;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService service;


    public PaymentController(
            PaymentService service
    ) {
        this.service = service;
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PostMapping(
            "/transactions/{transactionId}/payments"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(

            @PathVariable Long transactionId,

            @Valid
            @RequestBody
            CreatePaymentRequest request
    ) {

        return service.create(
                transactionId,
                request
        );
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @GetMapping(
            "/transactions/{transactionId}/payments"
    )
    public List<PaymentResponse> findByTransaction(

            @PathVariable Long transactionId
    ) {

        return service.findByTransaction(
                transactionId
        );
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @GetMapping(
            "/transactions/{transactionId}/payments/summary"
    )
    public TransactionPaymentSummary summary(

            @PathVariable Long transactionId
    ) {

        return service.getSummary(
                transactionId
        );
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @GetMapping(
            "/payments/{id}"
    )
    public PaymentResponse findById(

            @PathVariable Long id
    ) {

        return service.findById(id);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PatchMapping(
            "/payments/{id}/confirm"
    )
    public PaymentResponse confirm(

            @PathVariable Long id
    ) {

        return service.confirm(id);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PatchMapping(
            "/payments/{id}/fail"
    )
    public PaymentResponse fail(

            @PathVariable Long id
    ) {

        return service.fail(id);
    }


    @PreAuthorize(
            "hasRole('ADMIN')"
    )
    @PatchMapping(
            "/payments/{id}/refund"
    )
    public PaymentResponse refund(

            @PathVariable Long id
    ) {

        return service.refund(id);
    }
    @GetMapping("/payments/me")
    public List<PaymentResponse> myPayments() {

        return service.findMyPayments();
    }
}