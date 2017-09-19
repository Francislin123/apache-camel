package com.walmart.feeds.api.core.repository.blacklist.model;


import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "terms_blacklist")
public class TermsBlacklistEntity {

    @Id
    @GeneratedValue(generator = "terms_blacklist_uuid_generator")
    @GenericGenerator(name = "terms_blacklist_uuid_generator", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @ElementCollection
    @CollectionTable(name = "terms_blacklist_items", joinColumns = @JoinColumn(name = "blacklist_id", referencedColumnName = "id"))
    @Column(name="term", nullable = false)
    private Set<String> list;

    @Tolerate
    public TermsBlacklistEntity() {
        // Do nothing
    }

    @Builder
    public TermsBlacklistEntity(UUID id, String name, String slug, Set<String> list) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.list = list;
    }
}
