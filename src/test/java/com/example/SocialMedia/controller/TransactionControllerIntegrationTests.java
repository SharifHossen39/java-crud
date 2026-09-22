package com.example.SocialMedia.controller;

import com.example.SocialMedia.entity.User;
import com.example.SocialMedia.entity.Wallet;
import com.example.SocialMedia.repository.TransactionRepository;
import com.example.SocialMedia.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TransactionControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void createsTransactionWithUserAndWalletRelationships() throws Exception {
        User sender = userWithWallet("api-sender", "API-SENDER-WALLET");
        User receiver = userWithWallet("api-receiver", "API-RECEIVER-WALLET");
        userRepository.saveAndFlush(sender);
        userRepository.saveAndFlush(receiver);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/transactions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "transactionReference": "API-TXN-1001",
                          "amount": 100.00,
                          "type": "TRANSFER",
                          "status": "COMPLETED",
                          "description": "JPA relationship example",
                          "userId": %d,
                          "senderWalletId": %d,
                          "receiverWalletId": %d
                        }
                        """.formatted(
                        sender.getId(),
                        sender.getWallet().getId(),
                        receiver.getWallet().getId())))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("Transaction created successfully");
        assertThat(transactionRepository.findAll())
                .singleElement()
                .satisfies(transaction -> {
                    assertThat(transaction.getUser().getId()).isEqualTo(sender.getId());
                    assertThat(transaction.getSenderWallet().getId())
                            .isEqualTo(sender.getWallet().getId());
                    assertThat(transaction.getReceiverWallet().getId())
                            .isEqualTo(receiver.getWallet().getId());
                });
    }

    private User userWithWallet(String userName, String walletNumber) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(userName + "@example.com");

        Wallet wallet = new Wallet();
        wallet.setWalletNumber(walletNumber);
        wallet.setBalance(new BigDecimal("500.00"));
        wallet.setCurrency("BDT");
        wallet.setStatus("ACTIVE");
        wallet.setUser(user);
        user.setWallet(wallet);
        return user;
    }
}
