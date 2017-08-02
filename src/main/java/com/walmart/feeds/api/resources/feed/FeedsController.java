package com.walmart.feeds.api.resources.feed;

import com.walmart.feeds.api.core.converter.FeedConverter;
import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.Feed;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.service.feed.FeedService;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;
import com.walmart.feeds.api.resources.feed.response.ErrorResponse;
import com.walmart.feeds.api.resources.feed.response.FeedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ApiOperation(value = "Create a new feed",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful feed creation", response = ResponseEntity.class),
            @ApiResponse(code = 409, message = "Feed already exists"),
            @ApiResponse(code = 404, message = "Invalid partner")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createFeed(@Valid @RequestBody FeedRequest request, @PathVariable("partnerReference") String partnerReference, UriComponentsBuilder builder) throws NotFoundException {

        FeedTO feedTO = new ModelMapper().map(request, FeedTO.class);
        feedTO.setPartner(new PartnerTO());
        feedTO.getPartner().setReference(partnerReference);
        feedTO.setType(FeedType.getFromCode(request.getType()));

        feedService.createFeed(feedTO);

        UriComponents uriComponents =
                builder.path(V1_FEEDS.concat("/{reference}")).buildAndExpand(partnerReference, request.getReference());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }
    @ApiOperation(value = "Fetch feed by reference",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return found feed", response = FeedResponse.class),
            @ApiResponse(code = 404, message = "Invalid feed reference")})
    @RequestMapping(value = "{reference}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchFeed(@PathVariable("reference") String reference) {
        try {
            Feed feedEntity = feedRepository.findByReference(reference).orElseThrow(() -> new NotFoundException("Reference not found"));
            ModelMapper modelMapper = new ModelMapper();

            FeedResponse feedResponse = modelMapper.map(feedEntity, FeedResponse.class);

            return ResponseEntity.ok().body(feedResponse);

        }catch (NotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()));
        }
    }
    @ApiOperation(value = "Fetch all feeds by partner reference",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return found feeds", response = FeedResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Invalid partner reference")})
    @RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchAll(@PathVariable("partnerReference") String partnerReference, @RequestParam(value = "active", required = false) Boolean active) {
        try {
            List<FeedTO> listFeed = feedService.fetchByPartner(partnerReference, active);
            return ResponseEntity.ok(listFeed.stream().map(f -> FeedConverter.convertResponse(f)).collect(Collectors.toList()));
        } catch (NotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage()));
        }
    }

    @ApiOperation(value = "Change feed status",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Feed removed with success", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Invalid feed reference")})
    @RequestMapping(value = "{reference}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeFeedStatus(@PathVariable("reference") String reference, @RequestParam(value = "active") Boolean active) throws NotFoundException {
        feedService.changeFeedStatus(reference, active);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateFeed(@Valid @RequestBody FeedRequest request, @PathVariable("partnerReference") String partnerReference, UriComponentsBuilder builder) throws NotFoundException {
        FeedTO feedTO = new ModelMapper().map(request, FeedTO.class);
        feedTO.setPartner(new PartnerTO());
        feedTO.getPartner().setReference(partnerReference);
        feedTO.setType(FeedType.getFromCode(request.getType()));
        feedService.updateFeed(feedTO);
        UriComponents uriComponents =
                builder.path(V1_FEEDS.concat("/{reference}")).buildAndExpand(partnerReference, request.getReference());
        return ResponseEntity.ok(uriComponents.toUri());
    }
}
