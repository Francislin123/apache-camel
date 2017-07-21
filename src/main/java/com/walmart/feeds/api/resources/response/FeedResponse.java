package com.walmart.feeds.api.resources.response;

import com.walmart.feeds.api.resources.request.FeedType;
import com.walmart.feeds.api.resources.request.UTM;
import lombok.Data;

import java.util.List;

@Data
public class FeedResponse {

    private String id;
    private String name;
    private FeedType feedType;
    private List<UTM> utms;
}
