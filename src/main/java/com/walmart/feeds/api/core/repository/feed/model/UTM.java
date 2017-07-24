package com.walmart.feeds.api.core.repository.feed.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "feed_utm")
public class UTM {

    @Id
    private String id;

    @Column(name = "utm_type")
    private String type;

    @Column(name = "utm_value")
    private String value;

    @OneToOne
    @JoinColumn(name = "feed_id")
    private FeedEntity feed;
}
