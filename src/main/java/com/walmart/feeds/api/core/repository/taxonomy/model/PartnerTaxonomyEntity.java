package com.walmart.feeds.api.core.repository.taxonomy.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.resources.feed.validator.annotation.NotEmptyElements;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "file_name")
    private String fileName;

    @NotBlank
    @Column(name = "slug")
    private String slug;

    @NotNull
    @ManyToOne
    private PartnerEntity partner;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ImportStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "partnerTaxonomy")
    private List<TaxonomyMappingEntity> taxonomyMappings;

    @Builder
    public PartnerTaxonomyEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String fileName, String slug, PartnerEntity partner, ImportStatus status, List<TaxonomyMappingEntity> taxonomyMappings) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
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
