package com.aijobplatform.search.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponse {

    private Long refId;
    private String refType;
    private String title;
    private String content;
    private String tags;
}
