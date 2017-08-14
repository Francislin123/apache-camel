package com.walmart.feeds.api.core.repository.commercialstructure.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by vn0y942 on 08/08/17.
 */
@Entity
@Getter
@Table(name = "commercial_structure", uniqueConstraints = {@UniqueConstraint(columnNames = "slug")})
public class CommercialStructureEntity extends AuditableEntity{

    @Id
    @GeneratedValue(generator = "commercial_structure_uuid_generator")
    @GenericGenerator(name = "commercial_structure_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "archiveName")
    private String archiveName;

    @Column(name = "slug")
    private String slug;

    @ManyToOne
    private PartnerEntity partner;

    @OneToMany
    private List<CommercialStructureAssociationEntity> associationEntityList;

    @Builder
    private CommercialStructureEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String archiveName, String slug,
    PartnerEntity partner, List<CommercialStructureAssociationEntity> associationEntityList){
        super(creationDate, updateDate, user);
        this.id = id;
        this.archiveName = archiveName;
        this.slug = slug;
        this.partner = partner;
        this.associationEntityList = associationEntityList;
    }
}
