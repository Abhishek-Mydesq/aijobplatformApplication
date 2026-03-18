package com.aijobplatform.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class LoggingFilter implements GlobalFilter {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            org.springframework.cloud.gateway.filter.GatewayFilterChain chain
    ) {

        long start = System.currentTimeMillis();

        String path =
                exchange.getRequest().getURI().getPath();

        String method =
                exchange.getRequest().getMethod().name();

        log.info("➡ Request {} {}", method, path);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    long time =
                            System.currentTimeMillis() - start;

                    int status =
                            exchange.getResponse()
                                    .getStatusCode()
                                    .value();
                    log.info(
                            "⬅ Response {} {} -> {} ({} ms)",
                            method,
                            path,
                            status,
                            time
                    );

                }));
    }
}