package com.walmart.feeds.api.core.repository.partner.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "partner", uniqueConstraints = {
        @UniqueConstraint(name = "name_constraint", columnNames = "name"),
        @UniqueConstraint(name = "reference_constraint", columnNames = "reference")
})
@Data
public class Partner {

    @Id
    @GeneratedValue(generator = "system-uuid") // TODO migrate to GUID
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column
    private String name;

    @Column
    private String reference;

    @Column
    private String description;

    @OneToMany
    private List<Partnership> partnership;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationDate;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updateDate;

    @Column
    private boolean active;

}
