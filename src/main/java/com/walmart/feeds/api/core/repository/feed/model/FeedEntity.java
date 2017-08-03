package com.walmart.feeds.api.core.repository.feed.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
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
@Table(name = "feed", uniqueConstraints = {@UniqueConstraint(columnNames = "slug")})
public class FeedEntity extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "feed_uuid_generator")
    @GenericGenerator(name = "feed_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "name")
    private String name;

    // TODO[r0i001q]: verify if exists another way to set this field with a builder pattern
    @ManyToOne
    private PartnerEntity partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FeedType type;

    @Column(name = "notification_method")
    private String notificationMethod;

    @Column(name = "notification_format")
    private String notificationFormat;

    @Column(name = "notification_url")
    private String notificationUrl;

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
    private FeedEntity(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String slug, String name, PartnerEntity partner, FeedType type, String notificationMethod, String notificationFormat, String notificationUrl, Map<String, String> utms, boolean active) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.partner = partner;
        this.type = type;
        this.notificationMethod = notificationMethod;
        this.notificationFormat = notificationFormat;
        this.notificationUrl = notificationUrl;
        this.utms = utms;
        this.active = active;
    }

}

