package com.oms.gateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.oms.gateway.client.AuthServiceClient;
import com.oms.gateway.dto.ValidationResponse;

import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter
        implements GlobalFilter, Ordered {

    private final AuthServiceClient authClient;

    public JwtAuthenticationFilter(
            AuthServiceClient authClient) {

        this.authClient = authClient;

    }

    private static final List<String> OPEN_API = List.of(

            "/auth/login",

            "/auth/register",

            "/auth/refresh",

            "/eureka"

    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,

                             GatewayFilterChain chain) {

        String path = exchange.getRequest()

                .getURI()

                .getPath();

        if (OPEN_API.stream().anyMatch(path::startsWith)) {

            return chain.filter(exchange);

        }

        String authHeader =

                exchange.getRequest()

                        .getHeaders()

                        .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null ||

                !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()

                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();

        }

        String token = authHeader.substring(7);

        return authClient.validateToken(token)

                .flatMap(response -> {

                    if (!response.isValid()) {

                        exchange.getResponse()
                                .setStatusCode(HttpStatus.UNAUTHORIZED);

                        return exchange.getResponse().setComplete();

                    }

                    return chain.filter(exchange);

                });

    }

    @Override
    public int getOrder() {

        return -1;

    }

}
