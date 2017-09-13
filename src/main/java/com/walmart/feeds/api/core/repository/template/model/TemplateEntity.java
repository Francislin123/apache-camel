package com.walmart.feeds.api.core.repository.template.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@Table(name = "feed_template")
public class TemplateEntity {

    @Id
    @GeneratedValue(generator = "template_uuid_generator")
    @GenericGenerator(name = "template_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "name")
    private String name;

    @Column(name = "template")
    private String template;

    @Column(name = "separator")
    private String separator;

    @Column(name = "format")
    private String format;

    @Tolerate
    public TemplateEntity() {
        //default constructor for hibernate
    }

    @Builder
    public TemplateEntity(String slug, String name, String template, String separator, String format) {
        this.slug = slug;
        this.name = name;
        this.template = template;
        this.separator = separator;
        this.format = format;
    }
}

