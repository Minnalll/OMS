package com.oms.gateway.config;

import com.oms.gateway.filter.ApiKeyGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Bean
    public RouteLocator customRoutes(
            RouteLocatorBuilder builder,
            ApiKeyGatewayFilterFactory apiKeyFilter) {

        return builder.routes()

                .route("auth-service", r -> r
                        .path("/auth/**")
                        .filters(f -> f.filter(apiKeyFilter.apply(
                                new ApiKeyGatewayFilterFactory.Config())))
                        .uri("lb://AUTHSERVICE"))

                .route("product-service", r -> r
                        .path("/products/**")
                        .filters(f -> f.filter(apiKeyFilter.apply(
                                new ApiKeyGatewayFilterFactory.Config())))
                        .uri("lb://PRODUCTSERVICE"))

                .route("order-service", r -> r
                        .path("/orders/**")
                        .filters(f -> f.filter(apiKeyFilter.apply(
                                new ApiKeyGatewayFilterFactory.Config())))
                        .uri("lb://ORDERSERVICE"))

                .build();

    }

}