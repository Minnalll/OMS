package com.oms.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                // Auth Service
               /* .route("auth-service", route -> route
                        .path("/auth/**")
                        .uri("lb://AUTH-SERVICE"))*/

                // Product Service
                .route("product-service", route -> route
                        .path("/products/**")
                        .uri("lb://PRODUCTSERVICE"))

                // Order Service
                .route("order-service", route -> route
                        .path("/orders/**")
                        .uri("lb://ORDERSERVICE"))

                .build();
    }

}
