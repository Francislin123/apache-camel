package com.walmart.feeds.api.core.repository.commercialstructure.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by vn0y942 on 14/08/17.
 */
@Entity
@Getter
@Table(name = "cs_association")
public class CommercialStructureAssociationEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "cs_assoc_uuid_generator")
    @GenericGenerator(name = "cs_assoc_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "partner_cs_id")
    private String structurePartnerId;

    @Column(name = "partner_taxonomy")
    private String partnerTaxonomy;

    @Column(name = "walmart_taxonomy")
    private String walmartTaxonomy;

    @Builder
    public CommercialStructureAssociationEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id,
                                                String structurePartnerId, String partnerTaxonomy, String walmartTaxonomy){
        super(creationDate, updateDate, user);
        this.id = id;
        this.structurePartnerId = structurePartnerId;
        this.partnerTaxonomy = partnerTaxonomy;
        this.walmartTaxonomy = walmartTaxonomy;
    }

    @Tolerate
    public CommercialStructureAssociationEntity(){

    }
}
