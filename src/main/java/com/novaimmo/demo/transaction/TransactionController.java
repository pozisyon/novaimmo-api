package com.novaimmo.demo.transaction;

import com.novaimmo.demo.transaction.dto.CreateTransactionRequest;
import com.novaimmo.demo.transaction.dto.TransactionResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;


    public TransactionController(
            TransactionService service
    ) {
        this.service = service;
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(

            @Valid
            @RequestBody
            CreateTransactionRequest request
    ) {

        return service.create(request);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @GetMapping
    public List<TransactionResponse> findAll() {

        return service.findAll();
    }


    /*
     * CLIENT
     */
    @GetMapping("/me")
    public List<TransactionResponse> myTransactions() {

        return service.findMyTransactions();
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @GetMapping("/{id}")
    public TransactionResponse findById(
            @PathVariable Long id
    ) {

        return service.findById(id);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PatchMapping("/{id}/pending")
    public TransactionResponse pending(
            @PathVariable Long id
    ) {

        return service.markPending(id);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PatchMapping("/{id}/confirm")
    public TransactionResponse confirm(
            @PathVariable Long id
    ) {

        return service.confirm(id);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PatchMapping("/{id}/complete")
    public TransactionResponse complete(
            @PathVariable Long id
    ) {

        return service.complete(id);
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PatchMapping("/{id}/cancel")
    public TransactionResponse cancel(
            @PathVariable Long id
    ) {

        return service.cancel(id);
    }
}