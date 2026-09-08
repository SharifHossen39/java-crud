package com.example.SocialMedia.entity;

import com.example.SocialMedia.repository.TransactionRepository;
import com.example.SocialMedia.repository.UserRepository;
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
class UserTransactionRelationshipTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void userCanHaveManyTransactionsAndTransactionHasOneUser() {
        User sender = userWithWallet("sender", "SENDER-WALLET");
        User receiver = userWithWallet("receiver", "RECEIVER-WALLET");
        userRepository.saveAndFlush(sender);
        userRepository.saveAndFlush(receiver);

        Transaction first = transaction(
                "TXN-1001", sender, sender.getWallet(), receiver.getWallet());
        Transaction second = transaction(
                "TXN-1002", sender, sender.getWallet(), receiver.getWallet());
        transactionRepository.saveAllAndFlush(List.of(first, second));

        Long senderId = sender.getId();
        entityManager.clear();

        User reloadedSender = userRepository.findById(senderId).orElseThrow();
        assertThat(reloadedSender.getTransactions()).hasSize(2);
        assertThat(first.getUser().getId()).isEqualTo(senderId);
        assertThat(first.getSenderWallet().getWalletNumber()).isEqualTo("SENDER-WALLET");
        assertThat(first.getReceiverWallet().getWalletNumber()).isEqualTo("RECEIVER-WALLET");
        assertThat(first.getCreatedAt()).isNotNull();
    }

    private User userWithWallet(String userName, String walletNumber) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(userName + "@example.com");

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
            String reference, User user, Wallet senderWallet, Wallet receiverWallet) {
        Transaction transaction = new Transaction();
        transaction.setTransactionReference(reference);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType("TRANSFER");
        transaction.setStatus("COMPLETED");
        transaction.setDescription("Relationship test transfer");
        transaction.setUser(user);
        transaction.setSenderWallet(senderWallet);
        transaction.setReceiverWallet(receiverWallet);
        return transaction;
    }
}
