package com.walmart.feeds.api.core.repository.blacklist.model;

import com.walmart.feeds.api.core.repository.AuditableHistoryEntity;
import lombok.Builder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "terms_blacklist_history")
public class TermsBlacklistHistory extends AuditableHistoryEntity {

    @Id
    @GeneratedValue(generator = "terms_blacklist_uuid_generator")
    @GenericGenerator(name = "terms_blacklist_uuid_generator", strategy = "uuid2")
    @Column(name = "history_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "terms_blacklist_items")
    private String list;

    @Tolerate
    public TermsBlacklistHistory() {
        //default constructor for hibernate
    }

    @Builder
    public TermsBlacklistHistory(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String slug, String list) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.list = list;
    }
}
