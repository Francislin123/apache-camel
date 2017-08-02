package com.walmart.feeds.api.core.repository.partner.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "partner_history")
@Data
public class PartnerHistory extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "partner_history_uuid_generator")
    @GenericGenerator(name = "partner_history_uuid_generator", strategy = "uuid2")
    @Column(name = "history_id")
    private UUID id;

    @Column
    private String name;

    @Column(name = "reference")
    private String slug;

    @Column
    private String description;

    @Column
    private String partnerships;

    @Column(name = "flag_active")
    private boolean active;

    @PrePersist
    protected void prePersist() {
        this.creationDate = LocalDateTime.now();
        this.user = "teste";
    }

    @PreUpdate
    protected void preUpdate() {
        this.updateDate = LocalDateTime.now();
        this.user = "teste";
    }

}
