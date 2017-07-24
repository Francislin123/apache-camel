package com.walmart.feeds.api.core.service.feed.model;

import com.walmart.feeds.api.core.repository.feed.model.UTM;
import lombok.Data;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Data
public class FeedTO {

    private String id;
    private String name;
    private FeedType feedType;
    private List<UTM> utms;

}
