package com.walmart.feeds.api.core.repository.feed.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Created by vn0y942 on 21/07/17.
 */

@Entity
@Getter
@Table(name = "feed")
public class FeedEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "feed_uuid_generator")
    @GenericGenerator(name = "feed_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private PartnerEntity partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FeedType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_method")
    private FeedNotificationMethod notificationMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_format")
    private FeedNotificationFormat notificationFormat;

    @Column(name = "notification_url")
    private String notificationUrl;

    @ManyToOne
    private TemplateEntity template;

    @ElementCollection
    @MapKeyColumn(name = "utm_type")
    @CollectionTable(name = "feed_utms", joinColumns =
    @JoinColumn(name = "feed_id", referencedColumnName = "id"))
    @Column(name = "utm_value")
    private Map<String, String> utms;

    @Column(name = "flag_active")
    private boolean active;

    @Tolerate
    public FeedEntity() {
    }

    @Builder
    private FeedEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String slug, String name, PartnerEntity partner, FeedType type, FeedNotificationMethod notificationMethod, FeedNotificationFormat notificationFormat, String notificationUrl, TemplateEntity template, Map<String, String> utms, boolean active) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.partner = partner;
        this.type = type;
        this.notificationMethod = notificationMethod;
        this.notificationFormat = notificationFormat;
        this.notificationUrl = notificationUrl;
        this.template = template;
        this.utms = utms;
        this.active = active;
    }

}

