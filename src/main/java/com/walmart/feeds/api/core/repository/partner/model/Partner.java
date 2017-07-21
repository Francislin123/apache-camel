package com.walmart.feeds.api.core.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "partner")
@Data
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // TODO migrate to GUID
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String reference;

    @Column
    private String description;

    @OneToMany
    private List<Partnership> partnerships;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationDate;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updateDate;

    @Column
    private boolean active;

}
