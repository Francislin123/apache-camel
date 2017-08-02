package com.walmart.feeds.api.core.repository;


import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Calendar;

@Getter
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AuditableEntity {

    @Column(name = "creation_date")
    protected LocalDateTime creationDate;

    // TODO[r0i001q]: verify if exists another way to set this field with a builder pattern
    @Column(name = "update_date")
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
}

