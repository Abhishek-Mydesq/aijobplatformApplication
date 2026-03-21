package com.aijobplatform.search.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "search_index")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchIndex extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long refId;
    private String refType;
    private String title;
    @Column(length = 2000)
    private String content;
    private String tags;
}