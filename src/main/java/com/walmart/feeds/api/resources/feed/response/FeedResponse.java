package com.walmart.feeds.api.resources.feed.response;

import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.resources.feed.request.UTM;
import lombok.Data;

import java.util.List;

@Data
public class FeedResponse {

    private String reference;
    private String name;
    private FeedType feedType;
    private List<UTM> utms;

}
