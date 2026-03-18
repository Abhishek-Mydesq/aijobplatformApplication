package com.aijobplatform.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class JwtGatewayFilter implements GlobalFilter {

    private final JwtService jwtService;
    private final WebClient webClient;

    @Autowired
    public JwtGatewayFilter(
            JwtService jwtService,
            WebClient webClient
    ) {
        this.jwtService = jwtService;
        this.webClient = webClient;
    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain
    ) {

        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        // allow public
        if (path.contains("/api/auth")
                || path.contains("/eureka")
                || path.contains("/actuator")
                || path.contains("/api/users/token/validate")) {

            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {

            jwtService.validateToken(token);

            String email =
                    jwtService.extractEmail(token);

            return webClient
                    .get()
                    .uri("http://localhost:8081/api/users/token/validate?email="
                            + email + "&token=" + token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(valid -> {

                        if (valid == null || !valid) {

                            exchange.getResponse()
                                    .setStatusCode(HttpStatus.UNAUTHORIZED);

                            return exchange.getResponse().setComplete();
                        }

                        return chain.filter(exchange);
                    });

        } catch (Exception ex) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }
    }
}