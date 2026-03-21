package com.aijobplatform.search.kafka.consumer;
import com.aijobplatform.search.entity.SearchIndex;
import com.aijobplatform.search.kafka.dto.SearchEvent;
import com.aijobplatform.search.repository.SearchIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchConsumer {

    private final SearchIndexRepository repository;

    @KafkaListener(
            topics = "search-topic",
            groupId = "search-group",
            containerFactory = "searchKafkaListenerContainerFactory"
    )
    public void consume(SearchEvent event) {

        log.info("Search event received {}", event);

        SearchIndex index = SearchIndex.builder()
                .refId(event.getRefId())
                .refType(event.getRefType())
                .title(event.getTitle())
                .content(event.getContent())
                .tags(event.getTags())
                .build();

        repository.save(index);
    }

}