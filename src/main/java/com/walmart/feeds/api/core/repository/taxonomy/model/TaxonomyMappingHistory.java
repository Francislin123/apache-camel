package com.walmart.feeds.api.core.repository.taxonomy.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "taxonomy_mapping_history")
public class TaxonomyMappingHistory extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "tx_mapping_hist_uuid_generator")
    @GenericGenerator(name = "tx_mapping_hist_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "partner_path_id")
    private String partnerPathId;

    @Column(name = "partner_path")
    private String partnerPath;

    @Column(name = "walmart_path")
    private String walmartPath;

    @Builder
    public TaxonomyMappingHistory(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id,
                                  String partnerPathId, String partnerPath, String walmartPath){
        super(creationDate, updateDate, user);
        this.id = id;
        this.partnerPathId = partnerPathId;
        this.partnerPath = partnerPath;
        this.walmartPath = walmartPath;
    }

    @Tolerate
    public TaxonomyMappingHistory(){

    }
}
