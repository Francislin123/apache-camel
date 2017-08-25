package com.walmart.feeds.api.core.repository.blacklist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "taxonomy_blacklist_mapping")
@Getter
@JsonIgnoreProperties({"id"})
public class TaxonomyBlacklistMapping {

    @Id
    @GeneratedValue(generator = "taxonomy_bkl_mapping_uuid_generator")
    @GenericGenerator(name = "taxonomy_bkl_mapping_uuid_generator", strategy = "uuid2")
    @ApiModelProperty(hidden = true)
    private UUID id;

    @Column(nullable = false)
    private String taxonomy;

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
