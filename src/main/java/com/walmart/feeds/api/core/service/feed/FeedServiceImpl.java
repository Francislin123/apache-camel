package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */
public class FeedServiceImpl implements FeedService {

    @Autowired
    private FeedRepository feedRepository;



}
