package com.walmart.feeds.api.resources.feed;

import com.walmart.feeds.api.core.service.feed.FeedService;
import com.walmart.feeds.api.core.service.feed.FeedServiceImpl;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;
import com.walmart.feeds.api.resources.feed.response.FeedResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(FeedsController.V1_FEEDS)
public class FeedsController {

    static final String V1_FEEDS = "/v1/feeds";

    @Autowired
    private FeedService feedService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createFeed(@RequestBody FeedRequest request, UriComponentsBuilder builder) {

        FeedTO feedTO = new FeedTO();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(request, feedTO);

        feedService.createFeed(feedTO);

        UriComponents uriComponents =
                builder.path(V1_FEEDS.concat("/{id}")).buildAndExpand(request.getReference());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }

    @RequestMapping(value = "{feedId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedResponse> fetchFeed(@PathVariable("feedId") String feedId) {

        System.out.println(feedId);

        // TODO[r0i001q]: Call service to create feed

        return ResponseEntity.ok().build();
    }


}
