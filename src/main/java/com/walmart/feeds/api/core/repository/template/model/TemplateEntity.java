package com.walmart.feeds.api.core.repository.template.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;
@Data
@Entity
@Table(name = "feed_template")
@JsonIgnoreProperties(value = {"id"})
public class TemplateEntity {

    @Id
    @GeneratedValue(generator = "template_uuid_generator")
    @GenericGenerator(name = "template_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "slug")
    private String slug;



}

