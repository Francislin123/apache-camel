package com.walmart.feeds.api.core.repository.feed.model;

import org.hibernate.annotations.Table;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Entity
public class FeedEntity {

    @Id
    private String id;
    private String name;
    private FeedType feedType;
    private List<UTM> utms;
}
