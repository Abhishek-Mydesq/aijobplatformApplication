package com.aijobplatform.job.kafka.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchEvent {

    private Long refId;

    private String refType;

    private String title;

    private String content;

    private String tags;

}