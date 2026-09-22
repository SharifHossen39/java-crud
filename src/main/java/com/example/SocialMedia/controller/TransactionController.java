package com.example.SocialMedia.controller;

import com.example.SocialMedia.payload.request.TransactionRequest;
import com.example.SocialMedia.service.TransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public String createTransaction(@RequestBody TransactionRequest request) {
        transactionService.createTransaction(request);
        return "Transaction created successfully";
    }
}
