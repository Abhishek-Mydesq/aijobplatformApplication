package com.aijobplatform.gateway.exception;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(
            ServerWebExchange exchange,
            Throwable ex
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof RuntimeException) {
            status = HttpStatus.BAD_REQUEST;
        }

        String body = """
                {
                  "success": false,
                  "message": "%s"
                }
                """.formatted(ex.getMessage());

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse()
                .writeWith(
                        Mono.just(
                                exchange.getResponse()
                                        .bufferFactory()
                                        .wrap(bytes)
                        )
                );
    }
}