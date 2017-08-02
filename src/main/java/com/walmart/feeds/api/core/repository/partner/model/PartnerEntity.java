package com.walmart.feeds.api.core.repository.partner.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.*;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "partner", uniqueConstraints = {
        @UniqueConstraint(name = "name_constraint", columnNames = "name"),
        @UniqueConstraint(name = "reference_constraint", columnNames = "reference")
})
@Getter
public class PartnerEntity extends AuditableEntity {

    public static final String PARTNERSHOT_SEPARATOR = ";";

    @Id
    @GeneratedValue(generator = "partner_uuid_generator")
    @GenericGenerator(name = "partner_uuid_generator", strategy = "uuid2")
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

    @Tolerate
    public PartnerEntity() {
    }

    @Builder
    private PartnerEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String slug, String description, String partnerships, boolean active) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.partnerships = partnerships;
        this.active = active;
    }

    public List<String> getPartnershipsAsList() {
        return Arrays.asList(partnerships.split(PARTNERSHOT_SEPARATOR));
    }

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
