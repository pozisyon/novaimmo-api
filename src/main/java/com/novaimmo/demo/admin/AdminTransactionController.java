package com.novaimmo.demo.admin;

import com.novaimmo.demo.transaction.TransactionService;
import com.novaimmo.demo.transaction.dto.TransactionResponse;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/transactions")
public class AdminTransactionController {

    private final TransactionService transactionService;


    public AdminTransactionController(
            TransactionService transactionService
    ) {
        this.transactionService =
                transactionService;
    }


    @PatchMapping(
            "/{transactionId}/assign/{agentId}"
    )
    public TransactionResponse assignAgent(
            @PathVariable Long transactionId,
            @PathVariable Long agentId
    ) {

        return transactionService
                .assignAgent(
                        transactionId,
                        agentId
                );
    }
}