package com.example.SocialMedia.service;

import com.example.SocialMedia.entity.Transaction;
import com.example.SocialMedia.entity.Wallet;
import com.example.SocialMedia.payload.request.TransactionRequest;
import com.example.SocialMedia.repository.TransactionRepository;
import com.example.SocialMedia.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    public void createTransaction(TransactionRequest request) {
        Wallet senderWallet = walletRepository.findById(request.senderWalletId()).orElseThrow();
        Wallet receiverWallet = walletRepository.findById(request.receiverWalletId()).orElseThrow();

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
}
