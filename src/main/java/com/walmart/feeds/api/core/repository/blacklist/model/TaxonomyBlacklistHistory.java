package com.walmart.feeds.api.core.repository.blacklist.model;

import com.walmart.feeds.api.core.repository.AuditableHistoryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "taxonomy_blacklist_history")
public class TaxonomyBlacklistHistory extends AuditableHistoryEntity {

    @Id
    @GeneratedValue(generator = "taxonomy_blacklist_history_uuid_generator")
    @GenericGenerator(name = "taxonomy_blacklist_history_uuid_generator", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    /**
     * It stores a JSON to represent all taxonomies mapping
     */
    @Lob
    @Column
    private String list;

    @Builder
    public TaxonomyBlacklistHistory(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String slug, String list) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.list = list;
    }

    @Tolerate
    public TaxonomyBlacklistHistory() {
        // do nothing
    }
}
