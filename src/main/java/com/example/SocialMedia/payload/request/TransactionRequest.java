package com.example.SocialMedia.payload.request;

import com.example.SocialMedia.entity.TransactionStatus;
import com.example.SocialMedia.entity.TransactionType;

import java.math.BigDecimal;

public record TransactionRequest(
        String transactionReference,
        BigDecimal amount,
        TransactionType type,
        TransactionStatus status,
        String description,
        Long senderWalletId,
        Long receiverWalletId) {
}
