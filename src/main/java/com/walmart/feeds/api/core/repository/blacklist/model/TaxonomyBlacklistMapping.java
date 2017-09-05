package com.walmart.feeds.api.core.repository.blacklist.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "taxonomy_blacklist_mapping")
@Getter
public class TaxonomyBlacklistMapping {

    @Id
    @GeneratedValue(generator = "taxonomy_bkl_mapping_uuid_generator")
    @GenericGenerator(name = "taxonomy_bkl_mapping_uuid_generator", strategy = "uuid2")
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String taxonomy;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaxonomyOwner owner;

    @Tolerate
    public TaxonomyBlacklistMapping() {
    }

    @Builder
    public TaxonomyBlacklistMapping(UUID id, String taxonomy, TaxonomyOwner owner) {
        this.id = id;
        this.taxonomy = taxonomy;
        this.owner = owner;
    }
}
