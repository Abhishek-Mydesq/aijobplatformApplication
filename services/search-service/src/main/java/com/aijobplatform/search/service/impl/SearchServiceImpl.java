package com.aijobplatform.search.service.impl;
import com.aijobplatform.search.dto.SearchRequest;
import com.aijobplatform.search.dto.SearchResponse;
import com.aijobplatform.search.entity.SearchIndex;
import com.aijobplatform.search.repository.SearchIndexRepository;
import com.aijobplatform.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SearchIndexRepository repository;

    @Override
    public List<SearchResponse> search(SearchRequest request) {

        List<SearchIndex> list =
                repository.findByTitleContainingIgnoreCase(request.getKeyword());

        return list.stream()
                .map(e -> SearchResponse.builder()
                        .refId(e.getRefId())
                        .refType(e.getRefType())
                        .title(e.getTitle())
                        .content(e.getContent())
                        .tags(e.getTags())
                        .build())
                .toList();
    }
}