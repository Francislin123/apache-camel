package com.walmart.feeds.api.core.repository.partner.model;

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
public class Partner {

    @Id
    @GeneratedValue(generator = "partner_uuid_generator") // TODO migrate to GUID
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

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationDate;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updateDate;

    @Column(name = "flag_active")
    private boolean active;

}
