package com.example.SocialMedia.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleUsersControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Test
    void getUsersByRoleReturnsOnlyRequestedFieldsAndMatchingUsers() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> auditorResponse = httpClient.send(
                createUserRequest("""
                        {
                          "username": "auditor-one",
                          "email": "auditor@example.com",
                          "password": "secret",
                          "status": "ACTIVE",
                          "profile": {
                            "firstName": "Ayesha",
                            "lastName": "Rahman",
                            "gender": "Female"
                          },
                          "roles": [
                            {
                              "name": "AUDITOR"
                            }
                          ]
                        }
                        """),
                HttpResponse.BodyHandlers.ofString());

        HttpResponse<String> regularUserResponse = httpClient.send(
                createUserRequest("""
                        {
                          "username": "regular-one",
                          "email": "regular@example.com",
                          "password": "secret",
                          "status": "ACTIVE",
                          "profile": {
                            "firstName": "Regular",
                            "lastName": "User",
                            "gender": "Male"
                          },
                          "roles": [
                            {
                              "name": "USER"
                            }
                          ]
                        }
                        """),
                HttpResponse.BodyHandlers.ofString());

        assertThat(auditorResponse.statusCode()).isEqualTo(200);
        assertThat(regularUserResponse.statusCode()).isEqualTo(200);

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/roles/auditor/users"))
                .GET()
                .build();

        HttpResponse<String> getResponse = httpClient.send(
                getRequest,
                HttpResponse.BodyHandlers.ofString());

        assertThat(getResponse.statusCode()).isEqualTo(200);
        assertThat(getResponse.body())
                .contains("\"name\":\"Ayesha Rahman\"")
                .contains("\"gender\":\"Female\"")
                .contains("\"email\":\"auditor@example.com\"")
                .doesNotContain("regular@example.com")
                .doesNotContain("password")
                .doesNotContain("username");

        HttpRequest missingRoleRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/roles/DOES_NOT_EXIST/users"))
                .GET()
                .build();

        HttpResponse<String> missingRoleResponse = httpClient.send(
                missingRoleRequest,
                HttpResponse.BodyHandlers.ofString());

        assertThat(missingRoleResponse.statusCode()).isEqualTo(404);
    }

    private HttpRequest createUserRequest(String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/api/user/createUser"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }
}