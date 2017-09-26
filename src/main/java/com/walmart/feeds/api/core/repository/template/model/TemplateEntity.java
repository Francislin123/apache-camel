package com.walmart.feeds.api.core.repository.template.model;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Builder
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

    @Column(name = "header")
    private String header;

    @Column(name = "body")
    private String body;

    @Column(name = "footer")
    private String footer;

    @Column(name = "separator")
    private String separator;

    @Column(name = "format")
    private String format;

    @Tolerate
    public TemplateEntity() {
        //default constructor for hibernate
    }

}

