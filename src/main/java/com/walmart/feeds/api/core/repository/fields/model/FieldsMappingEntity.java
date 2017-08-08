package com.walmart.feeds.api.core.repository.fields.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by vn0gshm on 07/08/17.
 */
@Entity
@Getter
public class FieldsMappingEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "fields_mapping_uuid_generator")
    @GenericGenerator(name = "fields_mapping_uuid_generator", strategy = "uuid2")
    private UUID id;

    @Column
    private String name;

    @OneToMany
    private List<MappedField> mappedFields;

    @Builder
    private FieldsMappingEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
    }

    @Tolerate
    public FieldsMappingEntity() {
    }
}
