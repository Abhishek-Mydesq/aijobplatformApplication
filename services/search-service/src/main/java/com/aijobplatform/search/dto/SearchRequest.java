package com.aijobplatform.search.dto;
import lombok.Data;

@Data
public class SearchRequest {

    private String keyword;
    private int page = 0;
    private int size = 10;
}