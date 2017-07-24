package com.walmart.feeds.api.resources.feed.request;

import lombok.Data;

import java.util.List;

@Data
public class FeedRequest {

    private String reference;
    private String name;
    private FeedType feedType;
    private List<UTM> utms;


}
