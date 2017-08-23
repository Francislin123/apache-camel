package com.walmart.feeds.api.core.repository.taxonomy.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Table(name = "taxonomy_mapping")
public class TaxonomyMappingEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "tx_mapping_uuid_generator")
    @GenericGenerator(name = "tx_mapping_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @NotBlank
    @Column(name = "partner_path_id")
    private String partnerPathId;

    @NotBlank
    @Column(name = "partner_path")
    private String partnerPath;

    @NotBlank
    @Column(name = "walmart_path")
    private String walmartPath;

    @NotNull
    @ManyToOne
    private PartnerTaxonomyEntity partnerTaxonomy;

    @Builder
    public TaxonomyMappingEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String partnerPathId, String partnerPath, String walmartPath, PartnerTaxonomyEntity partnerTaxonomy) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.partnerPathId = partnerPathId;
        this.partnerPath = partnerPath;
        this.walmartPath = walmartPath;
        this.partnerTaxonomy = partnerTaxonomy;
    }

    @Tolerate
    public TaxonomyMappingEntity(){
        //default constructor for hibernate
    }
}
