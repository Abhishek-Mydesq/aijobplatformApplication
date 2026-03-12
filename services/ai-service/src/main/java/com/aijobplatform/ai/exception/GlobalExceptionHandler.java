package com.aijobplatform.ai.exception;
import com.aijobplatform.ai.common.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse<?>> handleResourceNotFound(ResourceNotFoundException ex) {
        APIResponse<?> response = new APIResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.badRequest().body(response);
    }
}