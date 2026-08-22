package com.oms.gateway.filter;

import com.oms.gateway.config.GatewaySecurityProperties;
import com.oms.gateway.constant.GatewayConstants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ApiKeyGatewayFilterFactory
        extends AbstractGatewayFilterFactory<ApiKeyGatewayFilterFactory.Config> {

    private final GatewaySecurityProperties gatewaySecurityProperties;

    public ApiKeyGatewayFilterFactory(GatewaySecurityProperties gatewaySecurityProperties) {
        super(Config.class);
        this.gatewaySecurityProperties = gatewaySecurityProperties;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            String apiKey = exchange.getRequest()
                    .getHeaders()
                    .getFirst(GatewayConstants.API_KEY_HEADER);

            if (apiKey == null || apiKey.isBlank()) {
                return unauthorized(exchange,
                        "API Key is missing.");
            }

            if (!gatewaySecurityProperties.getApiKey().equals(apiKey)) {
                return unauthorized(exchange, "Invalid API Key.");
            }

            return chain.filter(exchange);

        };
    }

    private reactor.core.publisher.Mono<Void> unauthorized(
            org.springframework.web.server.ServerWebExchange exchange,
            String message) {

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String json = """
                {
                  "status":401,
                  "error":"Unauthorized",
                  "message":"%s"
                }
                """.formatted(message);

        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        return exchange.getResponse()
                .writeWith(
                        reactor.core.publisher.Mono.just(
                                exchange.getResponse()
                                        .bufferFactory()
                                        .wrap(bytes)
                        )
                );
    }

    public static class Config {

    }

}