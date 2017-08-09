package com.walmart.feeds.api.core.repository.fields.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by vn0gshm on 07/08/17.
 */
@Entity
@Table(name = "fields_mapping")
@Getter
public class FieldsMappingEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "fields_mapping_uuid_generator")
    @GenericGenerator(name = "fields_mapping_uuid_generator", strategy = "uuid2")
    @JsonIgnore
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fields_mapping_id", referencedColumnName = "id")
    private List<MappedFieldEntity> mappedFields;

    @Builder
    public FieldsMappingEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String name, String slug, List<MappedFieldEntity> mappedFields) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.mappedFields = mappedFields;
    }

    @Tolerate
    public FieldsMappingEntity() {
    }
}
