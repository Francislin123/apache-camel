package com.walmart.feeds.api.core.repository.taxonomy.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "partner_taxonomy_history")
public class PartnerTaxonomyHistory extends AuditableEntity{

    @Id
    @GeneratedValue(generator = "partner_taxonomy_uuid_generator")
    @GenericGenerator(name = "partner_taxonomy_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "partner_taxonomy_id")
    private UUID partnerTaxonomyId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @ManyToOne
    private PartnerEntity partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ImportStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "partner_taxonomy_id", referencedColumnName = "id")
    private List<TaxonomyMappingHistory> taxonomyMappings;

    @Builder
    public PartnerTaxonomyHistory(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, UUID partnerTaxonomyId, String fileName, String name, String slug, PartnerEntity partner, ImportStatus status, List<TaxonomyMappingHistory> taxonomyMappings) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.partnerTaxonomyId = partnerTaxonomyId;
        this.fileName = fileName;
        this.name = name;
        this.slug = slug;
        this.partner = partner;
        this.status = status;
        this.taxonomyMappings = taxonomyMappings;
    }

    @Tolerate
    public PartnerTaxonomyHistory(){
        //default constructor for hibernate
    }
}
