package com.walmart.feeds.api.core.repository.blacklist.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
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
@Table(name = "taxonomy_blacklist")
public class TaxonomyBlacklistEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "taxonomy_blacklist_uuid_generator")
    @GenericGenerator(name = "taxonomy_blacklist_uuid_generator", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "taxonomy_blacklist_id", referencedColumnName = "id")
    private Set<TaxonomyBlacklistMapping> list;

    @Tolerate
    public TaxonomyBlacklistEntity() {
    }

    @Builder
    public TaxonomyBlacklistEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String slug, Set<TaxonomyBlacklistMapping> list) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.list = list;
    }
}
