package com.walmart.feeds.api.core.repository.blacklist.model;


import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
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

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String slug;

    @ElementCollection
    @CollectionTable(name = "terms_blacklist_items", joinColumns = @JoinColumn(name = "blacklist_id", referencedColumnName = "id"))
    @Column(name = "term", nullable = false)
    private Set<String> list;

    @ManyToMany(mappedBy = "termsBlacklist")
    private List<FeedEntity> feed;

    @Builder
    public TermsBlacklistEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String slug, Set<String> list, List<FeedEntity> feed) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.list = list;
        this.feed = feed;
    }

    @Tolerate
    public TermsBlacklistEntity() {
        // Do nothing
    }

}
