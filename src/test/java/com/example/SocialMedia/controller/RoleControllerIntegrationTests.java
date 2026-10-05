package com.example.SocialMedia.controller;

import com.example.SocialMedia.entity.Role;
import com.example.SocialMedia.repository.RoleRepository;
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
class RoleControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void createRoleAndRejectDuplicateName() throws Exception {
        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/roles/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                          "name": "support"
                        }
                        """))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> createResponse = httpClient.send(
                createRequest,
                HttpResponse.BodyHandlers.ofString());

        assertThat(createResponse.statusCode()).isEqualTo(201);
        assertThat(createResponse.body()).isEqualTo("Role Created Successfully");
        assertThat(roleRepository.findByNameIgnoreCase("SUPPORT"))
                .get()
                .extracting(Role::getName)
                .isEqualTo("support");

        HttpResponse<String> duplicateResponse = httpClient.send(
                createRequest,
                HttpResponse.BodyHandlers.ofString());

        assertThat(duplicateResponse.statusCode()).isEqualTo(409);
    }
}
