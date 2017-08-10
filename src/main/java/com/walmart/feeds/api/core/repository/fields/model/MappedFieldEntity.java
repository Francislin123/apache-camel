package com.walmart.feeds.api.core.repository.fields.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by vn0gshm on 07/08/17.
 */
@Entity
@Table(name = "mapped_field")
@Builder
@Getter
@JsonIgnoreProperties({"id"})
public class MappedFieldEntity {

    @Id
    @GeneratedValue(generator = "mapped_field_uuid_generator")
    @GenericGenerator(name = "mapped_field_uuid_generator", strategy = "uuid2")
    private UUID id;

    @NotBlank
    @Column
    private String wmField;

    @NotBlank
    @Column
    private String partnerField;

    @Column
    private boolean required;

    @Tolerate
    public MappedFieldEntity() {
    }
}
