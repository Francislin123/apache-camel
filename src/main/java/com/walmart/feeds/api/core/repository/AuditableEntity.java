package com.walmart.feeds.api.core.repository;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.walmart.feeds.api.resources.serializers.LocalDateTimeSerializer;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AuditableEntity {

    @Column(name = "creation_date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    protected LocalDateTime creationDate;

    @Column(name = "update_date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    protected LocalDateTime updateDate;

    @Column(name = "user_login")
    protected String user;

    public AuditableEntity() {
        this.user = "anyone";
    }

    public AuditableEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user) {
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.user = user;
    }

    @PrePersist
    protected void prePersist() {
        this.creationDate = LocalDateTime.now();
        this.user = "Frans";
    }

    @PreUpdate
    protected void preUpdate() {
        this.updateDate = LocalDateTime.now();
        this.user = "Frans";
    }
}

