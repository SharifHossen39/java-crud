package com.example.SocialMedia.payload.request;

import java.math.BigDecimal;

public record TransactionRequest(
        String transactionReference,
        BigDecimal amount,
        String type,
        String status,
        String description,
        Long userId,
        Long senderWalletId,
        Long receiverWalletId) {
}
