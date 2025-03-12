package org.example.nemo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import org.example.nemo.model.Submersible;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class SubmersibleControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private WebTestClient webTestClient;

  @BeforeEach
  void init() {
    webTestClient = MockMvcWebTestClient.bindToApplicationContext(webApplicationContext).build();
  }

  @Test
  @DisplayName("Can get last submersible position")
  public void canGetLastSubmersiblePosition() {
    webTestClient.get().uri("/api/submersible/position")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.x")
        .isEqualTo("0.0")
        .jsonPath("$.y")
        .isEqualTo("0.0");
  }

  @Test
  @DisplayName("Can add a submersible absolute position")
  public void canAddSubmersibleAbsolutePosition() {
    Submersible submersible = new Submersible(1.4, 4.1);
    webTestClient.post().uri("/api/submersible/position/absolute")
        .bodyValue(submersible)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.x")
        .isEqualTo("1.4")
        .jsonPath("$.y")
        .isEqualTo("4.1");
  }

  @Test
  @DisplayName("Can add a submersible relative position")
  public void canAddSubmersibleRelativePosition() {
    String bodyValue = """
      { "direction": "UP", "quantity": 4.1 }
    """;
    webTestClient.post().uri("/api/submersible/position/relative")
        .bodyValue(bodyValue)
        .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.x")
        .isEqualTo("0.0")
        .jsonPath("$.y")
        .isEqualTo("4.1");
  }

  @Test
  @DisplayName("Can add and position print all movement")
  public void canAddAndPositionPrintAllMovement() {
    Submersible submersible = new Submersible(1.4, 4.1);
    webTestClient.post().uri("/api/submersible/position/absolute")
        .bodyValue(submersible)
        .exchange()
        .expectStatus().isOk();
    ParameterizedTypeReference<LinkedHashMap<Integer, Submersible>> typeRef = new ParameterizedTypeReference<>() {};
    FluxExchangeResult<LinkedHashMap<Integer, Submersible>> result =
        webTestClient.get().uri("/api/submersible/position/all")
        .exchange()
        .expectStatus().isOk()
        .returnResult(typeRef);
    assertNotNull(result.getResponseBody());
    assertNotNull(result.getResponseBody().blockFirst());
    LinkedHashMap<Integer, Submersible> body = result.getResponseBody().blockFirst();
    assertEquals(2, body.size());
  }

  @Test
  @DisplayName("Can update submersible position")
  public void canUpdateSubmersiblePosition() {
    webTestClient.put().uri("/api/submersible/position/0")
        .bodyValue(new Submersible(1.4, 4.1))
        .exchange()
        .expectStatus().isOk();
    webTestClient.get().uri("/api/submersible/position")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.x")
        .isEqualTo("1.4")
        .jsonPath("$.y")
        .isEqualTo("4.1");
  }

}
