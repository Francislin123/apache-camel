package com.walmart.feeds.api.core.repository;


import lombok.Getter;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AuditableHistoryEntity extends AuditableEntity {

    @Tolerate
    public AuditableHistoryEntity() {
    }

    public AuditableHistoryEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user) {
        super(creationDate, updateDate, user);
    }

    @PrePersist
    protected void prePersist() {
        // do nothing
    }

    @PreUpdate
    protected void preUpdate() {
        // do nothing
    }
}

