package com.aijobplatform.search.kafka.dto;

import lombok.Data;

@Data
public class SearchEvent {

    private Long refId;

    private String refType;

    private String title;

    private String content;

    private String tags;

}