package com.example.SocialMedia.payload.response;

import com.example.SocialMedia.entity.TransactionStatus;
import com.example.SocialMedia.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        String transactionReference,
        BigDecimal amount,
        TransactionType type,
        TransactionStatus status,
        String description,
        LocalDateTime createdAt,
        Long senderWalletId,
        String senderWalletNumber,
        String senderUsername,
        Long receiverWalletId,
        String receiverWalletNumber,
        String receiverUsername
) {
}
