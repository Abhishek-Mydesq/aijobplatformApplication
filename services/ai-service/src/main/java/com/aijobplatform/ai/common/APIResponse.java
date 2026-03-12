package com.aijobplatform.ai.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> {

    private boolean success;
    private String message;
    private T data;
}