package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */

public interface FeedService {

    void createFeed(FeedTO feedTO);
    public List<FeedTO> fetch();
    public List<FeedTO> fetchActive(FeedTO feedTo);
    public List<FeedTO> fetchByPartner(FeedTO feedTO);
}
