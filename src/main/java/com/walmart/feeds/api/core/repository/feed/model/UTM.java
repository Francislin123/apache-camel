package com.walmart.feeds.api.core.repository.feed.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "feed_utm")
public class UTM {

    @Id
    @GeneratedValue(generator = "feed_utm_uuid_generator")
    @GenericGenerator(name = "feed_utm_uuid_generator", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "utm_type")
    private String type;

    @Column(name = "utm_value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private FeedEntity feed;
}
