package com.walmart.feeds.api.core.repository.partner.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "partner", uniqueConstraints = {
        @UniqueConstraint(name = "name_constraint", columnNames = "name"),
        @UniqueConstraint(name = "reference_constraint", columnNames = "reference")
})
@Data
public class Partner extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "partner_uuid_generator")
    @GenericGenerator(name = "partner_uuid_generator", strategy = "uuid2")
    private UUID id;

    @Column
    private String name;

    @Column
    private String reference;

    @Column
    private String description;

    @Column
    private String partnerships;

    @Column(name = "flag_active")
    private boolean active;

}
