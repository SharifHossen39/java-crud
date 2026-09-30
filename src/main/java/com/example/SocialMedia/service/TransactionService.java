package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.Transaction;
import com.example.SocialMedia.entity.Wallet;
import com.example.SocialMedia.payload.request.TransactionRequest;
import com.example.SocialMedia.payload.response.TransactionResponse;
import com.example.SocialMedia.repository.TransactionRepository;
import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.repository.WalletRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            WalletRepository walletRepository,
            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createTransaction(TransactionRequest request) {
        if (request.senderWalletId() == null || request.receiverWalletId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Both senderWalletId and receiverWalletId must be provided");
        }

        if (request.senderWalletId().equals(request.receiverWalletId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sender and receiver wallet cannot be the same");
        }

        if (request.transactionReference() != null &&
                transactionRepository.existsByTransactionReference(request.transactionReference())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transaction with reference '" + request.transactionReference() + "' already exists");
        }

        Wallet senderWallet = walletRepository.findById(request.senderWalletId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Sender wallet not found with id: " + request.senderWalletId()));

        Wallet receiverWallet = walletRepository.findById(request.receiverWalletId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Receiver wallet not found with id: " + request.receiverWalletId()));

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(request.transactionReference());
        transaction.setAmount(request.amount());
        transaction.setType(request.type());
        transaction.setStatus(request.status());
        transaction.setDescription(request.description());
        transaction.setSenderWallet(senderWallet);
        transaction.setReceiverWallet(receiverWallet);

        transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found with id: " + userId);
        }

        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        return transactions.stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getTransactionReference(),
                t.getAmount(),
                t.getType(),
                t.getStatus(),
                t.getDescription(),
                t.getCreatedAt(),
                t.getSenderWallet() != null ? t.getSenderWallet().getId() : null,
                t.getSenderWallet() != null ? t.getSenderWallet().getWalletNumber() : null,
                t.getSenderWallet() != null && t.getSenderWallet().getUser() != null ? t.getSenderWallet().getUser().getUsername() : null,
                t.getReceiverWallet() != null ? t.getReceiverWallet().getId() : null,
                t.getReceiverWallet() != null ? t.getReceiverWallet().getWalletNumber() : null,
                t.getReceiverWallet() != null && t.getReceiverWallet().getUser() != null ? t.getReceiverWallet().getUser().getUsername() : null
        );
    }
}
