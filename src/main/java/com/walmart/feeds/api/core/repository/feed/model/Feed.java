package com.walmart.feeds.api.core.repository.feed.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Data
@Entity
@Table(name = "feed", uniqueConstraints = {@UniqueConstraint(columnNames = "reference")})
public class Feed extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "feed_uuid_generator")
    @GenericGenerator(name = "feed_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Partner partner;

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
    @Column(name = "utm_value")
    private Map<String, String> utms;

    @Column(name = "flag_active")
    private boolean active;

    public Feed() {
        this.active = true;
    }
}
