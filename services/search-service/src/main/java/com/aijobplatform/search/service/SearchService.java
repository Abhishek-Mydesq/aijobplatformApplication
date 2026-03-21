package com.aijobplatform.search.service;

import com.aijobplatform.search.dto.SearchRequest;
import com.aijobplatform.search.dto.SearchResponse;

import java.util.List;

public interface SearchService {
    List<SearchResponse> search(SearchRequest request);

}