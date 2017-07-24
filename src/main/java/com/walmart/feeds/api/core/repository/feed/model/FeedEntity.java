package com.walmart.feeds.api.core.repository.feed.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Data
@Entity
@Table(name = "feed")
public class FeedEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "name")
    private String name;

    // FIXME: 24/07/17 Referenciar Objeto partner
    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "notification_strategy")
    private FeedGenerationStrategy strategy;

    @Column(name = "notification_method")
    private String notificationMethod;

    @Column(name = "notification_format")
    private String notificationFormat;

    @Column(name = "notification_url")
    private String notificationUrl;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<UTM> utms;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "flag_active")
    private boolean active;
}
