package com.example.SocialMedia.entity;

import com.example.SocialMedia.repository.TransactionRepository;
import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.repository.WalletRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class WalletTransactionRelationshipTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void walletCanHaveManySentAndReceivedTransactions() {
        User sender = userWithWallet("sender", "SENDER-WALLET");
        User receiver = userWithWallet("receiver", "RECEIVER-WALLET");
        userRepository.saveAndFlush(sender);
        userRepository.saveAndFlush(receiver);

        Transaction first = transaction(
                "TXN-1001", sender.getWallet(), receiver.getWallet());
        Transaction second = transaction(
                "TXN-1002", sender.getWallet(), receiver.getWallet());
        transactionRepository.saveAllAndFlush(List.of(first, second));

        Long senderWalletId = sender.getWallet().getId();
        Long receiverWalletId = receiver.getWallet().getId();
        entityManager.clear();

        Wallet reloadedSenderWallet = walletRepository.findById(senderWalletId).orElseThrow();
        Wallet reloadedReceiverWallet = walletRepository.findById(receiverWalletId).orElseThrow();
        assertThat(reloadedSenderWallet.getSentTransactions()).hasSize(2);
        assertThat(reloadedReceiverWallet.getReceivedTransactions()).hasSize(2);
        assertThat(reloadedSenderWallet.getSentTransactions().get(0).getReceiverWallet().getId())
                .isEqualTo(receiverWalletId);
        assertThat(reloadedSenderWallet.getSentTransactions().get(0).getCreatedAt()).isNotNull();
    }

    private User userWithWallet(String username, String walletNumber) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@example.com");

        Wallet wallet = new Wallet();
        wallet.setWalletNumber(walletNumber);
        wallet.setBalance(new BigDecimal("1000.00"));
        wallet.setCurrency("BDT");
        wallet.setStatus("ACTIVE");
        wallet.setUser(user);
        user.setWallet(wallet);
        return user;
    }

    private Transaction transaction(
            String reference, Wallet senderWallet, Wallet receiverWallet) {
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(reference);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(TransactionType.SEND_MONEY);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setDescription("Relationship test transfer");
        transaction.setSenderWallet(senderWallet);
        transaction.setReceiverWallet(receiverWallet);
        return transaction;
    }
}
