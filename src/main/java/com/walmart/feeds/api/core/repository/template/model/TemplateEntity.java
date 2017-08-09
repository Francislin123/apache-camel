package com.walmart.feeds.api.core.repository.template.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;
@Data
@Entity
@Table(name = "feed_template")
public class TemplateEntity {

    @Id
    @GeneratedValue(generator = "template_uuid_generator")
    @GenericGenerator(name = "template_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "slug")
    private String slug;



}

