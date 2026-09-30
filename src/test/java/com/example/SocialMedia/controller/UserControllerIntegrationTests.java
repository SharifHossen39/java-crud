package com.example.SocialMedia.controller;

import com.example.SocialMedia.entity.Role;
import com.example.SocialMedia.repository.RoleRepository;
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

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void createAndGetUserDetails() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/createUser"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "username": "Mehedi001",
                          "email": "mehedi@gmail.com",
                          "password": "1234",
                          "phoneNumber": "0123456789",
                          "status": "active",
                          "profile": {
                            "firstName": "Mehedi",
                            "lastName": "Hasan",
                            "dateOfBirth": "07/06/1998",
                            "gender": "Male",
                            "address": "Dhaka"
                          },
                          "wallet": {
                            "walletNumber": "WALLET-1001",
                            "balance": 500.00,
                            "currency": "BDT",
                            "status": "ACTIVE"
                          },
                          "roles": [
                            {
                              "name": "Admin"
                            }
                          ]
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
        assertThat(userRepository.findAll().get(0).getCreatedAt()).isNotNull();
        assertThat(roleRepository.findAll())
                .singleElement()
                .extracting(Role::getName)
                .isEqualTo("ADMIN");
        assertThat(userProfileRepository.findAll())
                .singleElement()
                .satisfies(profile -> {
                    assertThat(profile.getFirstName()).isEqualTo("Mehedi");
                    assertThat(profile.getGender()).isEqualTo("Male");
                    assertThat(profile.getUser().getId())
                            .isEqualTo(userRepository.findAll().get(0).getId());
                });
        assertThat(walletRepository.findAll())
                .singleElement()
                .satisfies(wallet -> {
                    assertThat(wallet.getWalletNumber()).isEqualTo("WALLET-1001");
                    assertThat(wallet.getBalance()).isEqualByComparingTo("500.00");
                    assertThat(wallet.getUser().getId()).isNotNull();
                });

        Long userId = userRepository.findAll().get(0).getId();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/" + userId))
                .GET()
                .build();

        HttpResponse<String> getResponse = HttpClient.newHttpClient()
                .send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(getResponse.statusCode()).isEqualTo(200);
        assertThat(getResponse.body())
                .contains("\"username\":\"Mehedi001\"")
                .contains("\"gender\":\"Male\"")
                .contains("\"walletNumber\":\"WALLET-1001\"")
                .contains("\"name\":\"ADMIN\"")
                .doesNotContain("\"password\"");

        HttpRequest walletRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/" + userId + "/wallet"))
                .GET()
                .build();

        HttpResponse<String> walletResponse = HttpClient.newHttpClient()
                .send(walletRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(walletResponse.statusCode()).isEqualTo(200);
        assertThat(walletResponse.body())
                .contains("\"walletNumber\":\"WALLET-1001\"")
                .contains("\"balance\":500.00")
                .contains("\"currency\":\"BDT\"")
                .contains("\"status\":\"ACTIVE\"")
                .doesNotContain("\"username\"")
                .doesNotContain("\"profile\"")
                .doesNotContain("\"roles\"");

        HttpRequest missingUserRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/999999"))
                .GET()
                .build();

        HttpResponse<String> missingUserResponse = HttpClient.newHttpClient()
                .send(missingUserRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(missingUserResponse.statusCode()).isEqualTo(404);

        HttpRequest missingWalletRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/999999/wallet"))
                .GET()
                .build();

        HttpResponse<String> missingWalletResponse = HttpClient.newHttpClient()
                .send(missingWalletRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(missingWalletResponse.statusCode()).isEqualTo(404);

        HttpRequest userByWalletRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/by-wallet/WALLET-1001"))
                .GET()
                .build();

        HttpResponse<String> userByWalletResponse = HttpClient.newHttpClient()
                .send(userByWalletRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(userByWalletResponse.statusCode()).isEqualTo(200);
        assertThat(userByWalletResponse.body())
                .contains("\"username\":\"Mehedi001\"")
                .contains("\"phoneNumber\":\"0123456789\"");
        assertThat(userByWalletResponse.body())
                .doesNotContain("\"password\"")
                .doesNotContain("\"balance\"")
                .doesNotContain("\"walletNumber\"");

        HttpRequest missingWalletUserRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/by-wallet/NON-EXISTENT"))
                .GET()
                .build();

        HttpResponse<String> missingWalletUserResponse = HttpClient.newHttpClient()
                .send(missingWalletUserRequest, HttpResponse.BodyHandlers.ofString());

        assertThat(missingWalletUserResponse.statusCode()).isEqualTo(404);
    }
}
