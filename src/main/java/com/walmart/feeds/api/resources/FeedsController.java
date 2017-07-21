package com.walmart.feeds.api.resources;

import com.walmart.feeds.api.resources.request.FeedRequest;
import com.walmart.feeds.api.resources.response.FeedResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(FeedsController.V1_FEEDS)
public class FeedsController {

    static final String V1_FEEDS = "/v1/feeds";

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedResponse> createFeed(@RequestBody FeedRequest request, UriComponentsBuilder builder) {

        System.out.println(request);

        // TODO[r0i001q]: Call service to create feed

        FeedResponse feedResponse = new FeedResponse();

        feedResponse.setFeedType(request.getFeedType());
        feedResponse.setId(request.getId());
        feedResponse.setName(request.getName());
        feedResponse.setUtms(request.getUtms());

        UriComponents uriComponents =
                builder.path(V1_FEEDS.concat("/{id}")).buildAndExpand(request.getId());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }

    @RequestMapping(value = "{feedId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedResponse> fetchFeed(@PathVariable("feedId") String feedId) {

        System.out.println(feedId);

        // TODO[r0i001q]: Call service to create feed

        return ResponseEntity.ok().build();
    }


}
