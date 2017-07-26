package com.walmart.feeds.api.resources.feed;

import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.service.feed.FeedService;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;
import com.walmart.feeds.api.resources.feed.response.FeedResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(FeedsController.V1_FEEDS)
public class FeedsController {

    static final String V1_FEEDS = "/v1/partners/{partnerReference}/feeds";

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedService feedService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createFeed(@Valid @RequestBody FeedRequest request, @PathVariable("partnerReference") String partnerReference, UriComponentsBuilder builder) {

        FeedTO feedTO = new ModelMapper().map(request, FeedTO.class);
        feedTO.setPartnerReference(partnerReference);
        feedTO.setType(FeedType.getFromCode(request.getType()));

        feedService.createFeed(feedTO);

        UriComponents uriComponents =
                builder.path(V1_FEEDS.concat("/{reference}")).buildAndExpand(partnerReference, request.getReference());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }

    @RequestMapping(value = "{reference}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedResponse> fetchFeed(@PathVariable("reference") String reference) {

        System.out.println(reference);

        FeedEntity feedEntity = feedRepository.findByReference(reference).orElseThrow(RuntimeException::new);

        FeedResponse feedResponse = new FeedResponse();

        feedResponse.setName(feedEntity.getName());
        feedResponse.setReference(feedEntity.getReference());

        FeedNotificationData notification = new FeedNotificationData();
        notification.setFormat(feedEntity.getNotificationFormat());
        notification.setMethod(feedEntity.getNotificationMethod());
        notification.setUrl(feedEntity.getNotificationUrl());

        feedResponse.setFeedType(feedEntity.getType());

        feedResponse.setUtms(feedEntity.getUtms().stream().map(utm -> {
            com.walmart.feeds.api.resources.feed.request.UTM utmResponse = new com.walmart.feeds.api.resources.feed.request.UTM();
            utmResponse.setType(utm.getType());
            utmResponse.setValue(utm.getValue());
            return utmResponse;
        }).collect(Collectors.toList()));

        return ResponseEntity.ok().body(feedResponse);
    }
    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FeedResponse>> fetchAll(@PathVariable("partnerReference") String partnerReference){
        ModelMapper mapper = new ModelMapper();
        FeedTO feedTO = new FeedTO();
        feedTO.setPartnerReference(partnerReference);
        List<FeedTO> listFeed = feedService.fetchByPartner(feedTO);
        return ResponseEntity.ok(listFeed.stream().map(f -> mapper.map(f, FeedResponse.class)).collect(Collectors.toList()));
    }
}
