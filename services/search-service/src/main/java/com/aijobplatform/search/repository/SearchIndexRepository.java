package com.aijobplatform.search.repository;
import com.aijobplatform.search.entity.SearchIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SearchIndexRepository extends JpaRepository<SearchIndex, Long> {

    List<SearchIndex> findByTitleContainingIgnoreCase(String keyword);

    List<SearchIndex> findByContentContainingIgnoreCase(String keyword);

    List<SearchIndex> findByTagsContainingIgnoreCase(String keyword);

}