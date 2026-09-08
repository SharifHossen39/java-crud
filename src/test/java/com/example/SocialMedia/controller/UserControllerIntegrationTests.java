package com.example.SocialMedia.controller;

import com.example.SocialMedia.repository.UserRepository;
import com.example.SocialMedia.repository.UserProfileRepository;
import com.example.SocialMedia.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void createUserPersistsTheUser() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/createUser"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "userName": "Mehedi001",
                          "email": "mehedi@gmail.com",
                          "password": "1234",
                          "phoneNumber": "0123456789",
                          "status": "active",
                          "userProfile": {
                            "firstName": "Mehedi",
                            "lastName": "Hasan",
                            "dateOfBirth": "07/06/1998",
                            "gender": "male"
                          },
                          "wallet": {
                            "walletNumber": "WALLET-1001",
                            "balance": 500.00,
                            "currency": "BDT",
                            "status": "ACTIVE"
                          }
                        }
                        """))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("User created successfully");
        assertThat(userRepository.findAll())
                .singleElement()
                .extracting(user -> user.getEmail())
                .isEqualTo("mehedi@gmail.com");
        assertThat(userProfileRepository.findAll())
                .singleElement()
                .satisfies(profile -> {
                    assertThat(profile.getFirstName()).isEqualTo("Mehedi");
                    assertThat(profile.getUser().getId()).isNotNull();
                });
        assertThat(walletRepository.findAll())
                .singleElement()
                .satisfies(wallet -> {
                    assertThat(wallet.getWalletNumber()).isEqualTo("WALLET-1001");
                    assertThat(wallet.getBalance()).isEqualByComparingTo("500.00");
                    assertThat(wallet.getUser().getId()).isNotNull();
                });
    }
}
