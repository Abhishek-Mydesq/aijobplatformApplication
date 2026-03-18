package com.aijobplatform.gateway.security;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class InternalTokenFilter implements GlobalFilter {

    private static final String INTERNAL_TOKEN =
            "internal-secret";

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            org.springframework.cloud.gateway.filter.GatewayFilterChain chain
    ) {

        String internal =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst("X-INTERNAL-TOKEN");

        if (internal != null &&
                internal.equals(INTERNAL_TOKEN)) {

            return chain.filter(exchange);
        }

        return chain.filter(exchange);
    }
}