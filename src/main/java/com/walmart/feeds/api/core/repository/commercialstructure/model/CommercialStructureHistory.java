package com.walmart.feeds.api.core.repository.commercialstructure.model;

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
@Table(name = "commercial_structure_history")
public class CommercialStructureHistory extends AuditableEntity{

    @Id
    @GeneratedValue(generator = "commercial_structure_uuid_generator")
    @GenericGenerator(name = "commercial_structure_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "commercial_structure_id")
    private UUID commercialStructureId;

    @Column(name = "name")
    private String archiveName;

    @Column(name = "slug")
    private String slug;

    @ManyToOne
    private PartnerEntity partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ImportStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "commercial_structure_id", referencedColumnName = "id")
    private List<CommercialStructureAssociationHistory> associationEntityList;

    @Builder
    public CommercialStructureHistory(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, UUID commercialStructureId, String archiveName, String slug, PartnerEntity partner, ImportStatus status, List<CommercialStructureAssociationHistory> associationEntityList) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.commercialStructureId = commercialStructureId;
        this.archiveName = archiveName;
        this.slug = slug;
        this.partner = partner;
        this.status = status;
        this.associationEntityList = associationEntityList;
    }

    @Tolerate
    public CommercialStructureHistory(){

    }
}
