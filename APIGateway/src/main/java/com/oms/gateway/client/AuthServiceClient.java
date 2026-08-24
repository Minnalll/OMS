package com.oms.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.oms.gateway.dto.ValidationResponse;
import reactor.core.publisher.Mono;

/*@Component
public class AuthServiceClient {

    private final WebClient.Builder webClient;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public AuthServiceClient(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    public ValidationResponse validateToken(String token) {

        return webClient.build()

                .get()

                .uri(authServiceUrl + "/auth/validate")

                .header("Authorization", "Bearer " + token)

                .retrieve()

                .bodyToMono(ValidationResponse.class)

                .block();

    }
}*/
@Component
public class AuthServiceClient {

    private final WebClient.Builder webClient;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public AuthServiceClient(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    public Mono<ValidationResponse> validateToken(String token) {

        return webClient.build()
                .get()
                .uri(authServiceUrl + "/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(ValidationResponse.class);
    }
}