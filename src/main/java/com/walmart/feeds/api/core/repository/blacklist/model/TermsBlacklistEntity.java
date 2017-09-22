package com.walmart.feeds.api.core.repository.blacklist.model;


import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Table(name = "terms_blacklist")
public class TermsBlacklistEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "terms_blacklist_uuid_generator")
    @GenericGenerator(name = "terms_blacklist_uuid_generator", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @NotEmptyElements
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "blacklist_id", referencedColumnName = "id")
    @Column(name="term", nullable = false)
    private Set<String> list;


    @Builder
    public TermsBlacklistEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String slug, Set<String> list) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.list = list;
    }

    @Tolerate
    public TermsBlacklistEntity() {
        // Do nothing
    }

}
