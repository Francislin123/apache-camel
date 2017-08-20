package com.walmart.feeds.api.core.repository.taxonomy.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode(callSuper = true, exclude = "taxonomyMappings")
@Table(name = "partner_taxonomy", uniqueConstraints = {@UniqueConstraint(columnNames = "slug")})
public class PartnerTaxonomyEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "partner_taxonomy_uuid_generator")
    @GenericGenerator(name = "partner_taxonomy_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "slug")
    private String slug;

    @ManyToOne
    private PartnerEntity partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ImportStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "partnerTaxonomy")
    private List<TaxonomyMappingEntity> taxonomyMappings;

    @Builder
    public PartnerTaxonomyEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String fileName, String slug, PartnerEntity partner, ImportStatus status, List<TaxonomyMappingEntity> taxonomyMappings) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.fileName = fileName;
        this.slug = slug;
        this.partner = partner;
        this.status = status;
        this.taxonomyMappings = taxonomyMappings;
    }

    @Tolerate
    public PartnerTaxonomyEntity(){

    }
}
