package com.walmart.feeds.api.resources.feed;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.service.feed.FeedService;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;
import com.walmart.feeds.api.resources.feed.response.ErrorResponse;
import com.walmart.feeds.api.resources.feed.response.FeedResponse;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@RequestMapping(FeedsController.V1_FEEDS)
public class FeedsController {

    static final String V1_FEEDS = "/v1/partners/{partnerReference}/feeds";

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private FeedService feedService;

    @ApiOperation(value = " Create a new feed ",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful feed creation", response = ResponseEntity.class),
            @ApiResponse(code = 409, message = "Feed already exists"),
            @ApiResponse(code = 404, message = "Invalid partner")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createFeed(@Valid @RequestBody FeedRequest request, @PathVariable("partnerReference") String partnerReference, UriComponentsBuilder builder) {

        try {
            FeedTO feedTO = new ModelMapper().map(request, FeedTO.class);
            feedTO.setPartnerReference(partnerReference);
            feedTO.setType(FeedType.getFromCode(request.getType()));

            feedService.createFeed(feedTO);

            UriComponents uriComponents =
                    builder.path(V1_FEEDS.concat("/{reference}")).buildAndExpand(partnerReference, request.getReference());

            return ResponseEntity.created(uriComponents.toUri()).build();

            // TODO[r0i001q]: Use exception handler
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    @ApiOperation(value = " Fetch feed by reference ",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return found feed", response = FeedResponse.class),
            @ApiResponse(code = 404, message = "Invalid feed reference")})
    @RequestMapping(value = "{reference}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchFeed(@PathVariable("reference") String reference) {
        try {
            FeedEntity feedEntity = feedRepository.findByReference(reference).orElseThrow(() -> new NotFoundException("Reference not found"));

            FeedResponse feedResponse = new FeedResponse();

            feedResponse.setName(feedEntity.getName());
            feedResponse.setReference(feedEntity.getReference());

            FeedNotificationData notification = new FeedNotificationData();
            notification.setFormat(feedEntity.getNotificationFormat());
            notification.setMethod(feedEntity.getNotificationMethod());
            notification.setUrl(feedEntity.getNotificationUrl());

            feedResponse.setFeedType(feedEntity.getType());

            feedResponse.setUtms(feedEntity.getUtms().stream().map(utm -> {
                com.walmart.feeds.api.resources.feed.request.UTM utmResponse = new com.walmart.feeds.api.resources.feed.request.UTM(utm.getType(), utm.getValue());
                return utmResponse;
            }).collect(Collectors.toList()));

            return ResponseEntity.ok().body(feedResponse);

        }catch (NotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()));
        }
    }
    @ApiOperation(value = " Fetch all feeds by partner reference ",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return found feeds", response = FeedResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Invalid partner reference")})
    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchAll(@PathVariable("partnerReference") String partnerReference) {
        List<FeedTO> listFeed = null;
        ModelMapper mapper = new ModelMapper();
        try {
            FeedTO feedTO = new FeedTO();
            feedTO.setPartnerReference(partnerReference);
            listFeed = feedService.fetchByPartner(feedTO);
            return ResponseEntity.ok(listFeed.stream().map(f -> mapper.map(f, FeedResponse.class)).collect(Collectors.toList()));
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()));
        }
    }
    @ApiOperation(value = "Remove feed by reference",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Feed removed with success", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Invalid feed reference")})
    @RequestMapping (value = "{reference}",method=RequestMethod.PATCH,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeFeed(@PathVariable("reference")String reference){
        try {
            feedService.removeFeed(reference);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @ApiOperation(value = "Fetch actives feeds",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return all actives feeds", response = FeedResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Invalid partner reference")})
    @RequestMapping(value = "actives", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchActives(@PathVariable("partnerReference") String partnerReference){
        List<FeedTO> listFeed = null;
        ModelMapper mapper = new ModelMapper();
        try {
            FeedTO feedTO = new FeedTO();
            feedTO.setPartnerReference(partnerReference);
            listFeed = feedService.fetchByActiveAndByPartner(feedTO);
            return ResponseEntity.ok(listFeed.stream().map(f -> mapper.map(f, FeedResponse.class)).collect(Collectors.toList()));
        }catch (NotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()));
        }

    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity patchFeed(@Valid @RequestBody FeedRequest request, @PathVariable("partnerReference") String partnerReference, UriComponentsBuilder builder){
        try{
            FeedTO feedTO = new ModelMapper().map(request, FeedTO.class);
            feedTO.setPartnerReference(partnerReference);
            feedTO.setType(FeedType.getFromCode(request.getType()));
            feedService.updateFeed(feedTO);
            UriComponents uriComponents =
                    builder.path(V1_FEEDS.concat("/{reference}")).buildAndExpand(partnerReference, request.getReference());
            return ResponseEntity.ok(uriComponents.toUri());
        }catch (DataIntegrityViolationException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
