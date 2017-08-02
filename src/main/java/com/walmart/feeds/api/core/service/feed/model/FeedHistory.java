package com.walmart.feeds.api.core.service.feed.model;

import com.walmart.feeds.api.core.repository.AuditableEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by vn0gshm on 01/08/17.
 */

@Entity
@Table(name = "feed_history")
@Data
public class FeedHistory extends AuditableEntity {

    @Id
    @GeneratedValue(generator = "feed_uuid_generator")
    @GenericGenerator(name = "feed_uuid_generator", strategy = "uuid2")
    @Column(name = "history_id")
    private UUID id;

    @Column(name = "reference")
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

    public FeedHistory() {
        this.active = true;
    }

}
