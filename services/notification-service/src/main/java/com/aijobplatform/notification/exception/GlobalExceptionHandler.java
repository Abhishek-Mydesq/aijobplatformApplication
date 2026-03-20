package com.aijobplatform.notification.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(
            ResourceNotFoundException ex
    ) {

        return build(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(
            Exception ex
    ) {

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
    }


    private ResponseEntity<?> build(
            HttpStatus status,
            String message
    ) {

        Map<String, Object> map = new HashMap<>();

        map.put("time", LocalDateTime.now());
        map.put("status", status.value());
        map.put("error", message);

        return new ResponseEntity<>(
                map,
                status
        );
    }

}