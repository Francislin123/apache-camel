package com.walmart.feeds.api.core.service.feed.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.AuditableHistoryEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import lombok.Builder;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by vn0gshm on 01/08/17.
 */

@Entity
@Table(name = "feed_history")
public class FeedHistory extends AuditableHistoryEntity {

    @Id
    @GeneratedValue(generator = "feed_uuid_generator")
    @GenericGenerator(name = "feed_uuid_generator", strategy = "uuid2")
    @Column(name = "history_id")
    private UUID id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "name")
    private String name;

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

    @Column(name = "flag_active")
    private boolean active;

    @Tolerate
    public FeedHistory() {
        this.active = true;
    }

    @Builder
    public FeedHistory(LocalDateTime creationDate, LocalDateTime updateDate, String user, UUID id, String slug, String name, PartnerEntity partner, FeedType type, String notificationMethod, String notificationFormat, String notificationUrl, boolean active) {
        super(creationDate, updateDate, user);
        this.id = id;
        this.slug = slug;
        this.name = name;
        this.partner = partner;
        this.type = type;
        this.notificationMethod = notificationMethod;
        this.notificationFormat = notificationFormat;
        this.notificationUrl = notificationUrl;
        this.active = active;
    }

}
