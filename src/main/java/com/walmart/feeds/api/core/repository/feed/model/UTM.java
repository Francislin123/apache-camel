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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id")
    private UUID id;

    @Column(name = "utm_type")
    private String type;

    @Column(name = "utm_value")
    private String value;

    @OneToOne
    @JoinColumn(name = "feed_id")
    private FeedEntity feed;
}
