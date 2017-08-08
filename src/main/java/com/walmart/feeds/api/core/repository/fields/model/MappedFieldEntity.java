package com.walmart.feeds.api.core.repository.fields.model;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

/**
 * Created by vn0gshm on 07/08/17.
 */
@Entity
@Builder
@Getter
public class MappedFieldEntity {

    @Id
    @GeneratedValue(generator = "mapped_field_uuid_generator")
    @GenericGenerator(name = "mapped_field_uuid_generator", strategy = "uuid2")
    private UUID id;

    @Column
    private String wmField;

    @Column
    private String partnerField;

    @Column
    private boolean required;

}
