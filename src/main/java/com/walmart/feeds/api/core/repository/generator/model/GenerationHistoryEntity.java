package com.walmart.feeds.api.core.repository.generator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "generation_history")
@Getter
@Builder
public class GenerationHistoryEntity {

    @Id
    @GeneratedValue(generator = "feed_generation_uuid_generator")
    @GenericGenerator(name = "feed_generation_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "partner_slug")
    private String partnerSlug;

    @Column(name = "feed_slug")
    private String feedSlug;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "total_skus")
    private int totalSkus;

    @Column(name = "generation_date")
    private LocalDateTime generationDate;

    @Tolerate
    public GenerationHistoryEntity() {
        // do nothing
    }

}
