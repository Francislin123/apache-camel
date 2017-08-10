package com.walmart.feeds.api.core.repository.commercialstructure.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by vn0y942 on 08/08/17.
 */
@Entity
@Getter
@Table(name = "commercial_structure", uniqueConstraints = {@UniqueConstraint(columnNames = "slug")})
public class CommercialStructureEntity extends AuditableEntity{

    @Id
    @GeneratedValue(generator = "feed_uuid_generator")
    @GenericGenerator(name = "feed_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "archiveName")
    private String archiveName;

    @Column(name = "slug")
    private String slug;

    @Column(name = "structure_partner_id")
    private String structurePartnerId;

    @Column(name = "partner_taxonomy")
    private String partnerTaxonomy;

    @Column(name = "walmart_taxonomy")
    private String walmartTaxonomy;

    @ManyToOne
    private PartnerEntity partner;

    @Builder
    private CommercialStructureEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String archiveName, String slug,
    String structurePartnerId, String partnerTaxonomy, String walmartTaxonomy, PartnerEntity partner){
        super(creationDate, updateDate, user);
        this.id = id;
        this.archiveName = archiveName;
        this.slug = slug;
        this.structurePartnerId = structurePartnerId;
        this.partnerTaxonomy = partnerTaxonomy;
        this.walmartTaxonomy = walmartTaxonomy;
        this.partner = partner;
    }
}
