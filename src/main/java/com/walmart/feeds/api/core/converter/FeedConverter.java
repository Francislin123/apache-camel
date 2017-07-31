package com.walmart.feeds.api.core.converter;

import com.walmart.feeds.api.core.repository.feed.model.Feed;
import com.walmart.feeds.api.core.service.feed.model.FeedNotificationDataTO;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.resources.feed.response.FeedResponse;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import org.modelmapper.ModelMapper;


/**
 * Created by vn0y942 on 28/07/17.
 */
public class FeedConverter {
    
    public static Feed convert(FeedTO feedTO){
        ModelMapper mapper = new ModelMapper();
        Feed feed = mapper.map(feedTO, Feed.class);
        feed.setNotificationFormat(feedTO.getNotificationData().getFormat());
        feed.setNotificationMethod(feedTO.getNotificationData().getMethod());
        feed.setNotificationUrl(feedTO.getNotificationData().getUrl());
        return feed;
    }
    public static FeedTO convert(Feed feed){
        ModelMapper mapper  = new ModelMapper();
        FeedTO feedTO = mapper.map(feed, FeedTO.class);
        feedTO.setNotificationData(new FeedNotificationDataTO(feed.getNotificationMethod(),
                feed.getNotificationFormat(), feed.getNotificationUrl()));
        return feedTO;

    }
    public static FeedResponse convertResponse(FeedTO feedTO){
        ModelMapper modelMapper = new ModelMapper();
        FeedResponse response = modelMapper.map(feedTO, FeedResponse.class);
        response.setPartner(modelMapper.map(feedTO.getPartner(), PartnerResponse.class));
        return response;
    }

}
