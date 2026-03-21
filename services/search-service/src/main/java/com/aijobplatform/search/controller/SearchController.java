package com.aijobplatform.search.controller;
import com.aijobplatform.search.common.ApiResponse;
import com.aijobplatform.search.dto.SearchRequest;
import com.aijobplatform.search.dto.SearchResponse;
import com.aijobplatform.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public ApiResponse<List<SearchResponse>> search(
            @RequestBody SearchRequest request
    ) {
        return ApiResponse.success(
                searchService.search(request)
        );
    }

}