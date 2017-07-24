package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import org.springframework.stereotype.Service;

/**
 * Created by vn0y942 on 21/07/17.
 */

@Service
public interface FeedService {

    FeedTO createFeed(FeedTO feedTO);

}
