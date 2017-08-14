package com.walmart.feeds.api.core.repository.commercialstructure.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by vn0y942 on 14/08/17.
 */
@Entity
@Getter
@Table(name = "cs_association", uniqueConstraints = {@UniqueConstraint(columnNames = "slug")})
public class CommercialStructureAssociationEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "feed_uuid_generator")
    @GenericGenerator(name = "feed_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "structure_partner_id")
    private String structurePartnerId;

    @Column(name = "partner_taxonomy")
    private String partnerTaxonomy;

    @Column(name = "walmart_taxonomy")
    private String walmartTaxonomy;

    @ManyToOne
    private CommercialStructureEntity commercialStructureEntity;

    public CommercialStructureAssociationEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id,
                                                String structurePartnerId, String partnerTaxonomy, String walmartTaxonomy,
                                                CommercialStructureEntity commercialStructureEntity){
        super(creationDate, updateDate, user);
        this.id = id;
        this.structurePartnerId = structurePartnerId;
        this.partnerTaxonomy = partnerTaxonomy;
        this.walmartTaxonomy = walmartTaxonomy;
        this.commercialStructureEntity = commercialStructureEntity;
    }
}
