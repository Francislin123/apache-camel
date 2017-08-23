package com.walmart.feeds.api.core.repository.fields.model;

import com.walmart.feeds.api.core.repository.AuditableHistoryEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by vn0gshm on 07/08/17.
 */
@Entity
@Getter
public class FieldsMappingHistory extends AuditableHistoryEntity {

    @Id
    @GeneratedValue(generator = "fields_mapping_uuid_generator")
    @GenericGenerator(name = "fields_mapping_uuid_generator", strategy = "uuid2")
    @Column(name = "history_id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Lob
    @Column(name = "mapped_fields")
    private String mappedFields;

    @Builder
    public FieldsMappingHistory(LocalDateTime creationDate, LocalDateTime updateDate, String user,
                                UUID id, String name, String slug, String mappedFields) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.mappedFields = mappedFields;
    }

    @Tolerate
    public FieldsMappingHistory() {
        //default constructor for hibernate
    }
}
