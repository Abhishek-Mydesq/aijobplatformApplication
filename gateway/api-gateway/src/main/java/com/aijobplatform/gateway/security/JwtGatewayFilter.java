package com.aijobplatform.gateway.security;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
@Order(-1)
public class JwtGatewayFilter implements GlobalFilter {

    private final JwtService jwtService;

    public JwtGatewayFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();

        if (path.startsWith("/api/auth")
                || path.startsWith("/eureka")
                || path.startsWith("/actuator")) {

            return chain.filter(exchange);
        }

        String authHeader =
                request.getHeaders().getFirst("Authorization");

        System.out.println("HEADER = " + authHeader);

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println("NO TOKEN");

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {

            System.out.println("TOKEN = " + token);

            jwtService.validateToken(token);

            System.out.println("TOKEN VALID");

        } catch (Exception ex) {

            System.out.println("TOKEN ERROR = " + ex.getMessage());

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}