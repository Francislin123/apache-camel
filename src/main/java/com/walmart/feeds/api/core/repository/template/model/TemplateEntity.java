package com.walmart.feeds.api.core.repository.template.model;

import lombok.Builder;
import lombok.Getter;
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

    @Column(name = "slug" , unique = true)
    private String slug;

    public TemplateEntity() {
    }

    @Builder
    public TemplateEntity(UUID id, String slug) {
        this.id = id;
        this.slug = slug;
    }
}

