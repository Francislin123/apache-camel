package com.walmart.feeds.api.core.repository.partner.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.Builder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "partner_history")
public class PartnerHistory extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "partner_history_uuid_generator")
    @GenericGenerator(name = "partner_history_uuid_generator", strategy = "uuid2")
    @Column(name = "history_id")
    private UUID id;

    @Column
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column
    private String description;

    @Column
    private String partnerships;

    @Column(name = "flag_active")
    private boolean active;

    @Tolerate
    public PartnerHistory() {
    }

    @Builder
    public PartnerHistory(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String slug, String description, String partnerships, boolean active) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.partnerships = partnerships;
        this.active = active;
    }

    @Override
    protected void prePersist() {
        //do-nothing
    }

    @Override
    protected void preUpdate() {
        //do-nothing
    }
}
