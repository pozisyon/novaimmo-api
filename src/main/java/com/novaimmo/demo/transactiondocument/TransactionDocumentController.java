package com.novaimmo.demo.transactiondocument;

import com.novaimmo.demo.transactiondocument.dto.CreateTransactionDocumentRequest;
import com.novaimmo.demo.transactiondocument.dto.TransactionDocumentResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionDocumentController {

    private final TransactionDocumentService service;


    public TransactionDocumentController(
            TransactionDocumentService service
    ) {

        this.service = service;
    }


    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PostMapping(
            "/transactions/{transactionId}/documents"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDocumentResponse create(

            @PathVariable Long transactionId,

            @Valid
            @RequestBody
            CreateTransactionDocumentRequest request
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
            "/transactions/{transactionId}/documents"
    )
    public List<TransactionDocumentResponse> findByTransaction(

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
            "/transaction-documents/{id}"
    )
    public TransactionDocumentResponse findById(

            @PathVariable Long id
    ) {

        return service.findById(id);
    }


    @PreAuthorize(
            "hasRole('ADMIN')"
    )
    @DeleteMapping(
            "/transaction-documents/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(

            @PathVariable Long id
    ) {

        service.delete(id);
    }
    @GetMapping("/transaction-documents/me")
    public List<TransactionDocumentResponse> myDocuments() {

        return service.findMyDocuments();
    }
}