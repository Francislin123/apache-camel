package com.walmart.feeds.api.resources.feed;

import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationFormat;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.feed.FeedService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.feed.request.FeedNotificationData;
import com.walmart.feeds.api.resources.feed.request.FeedRequest;
import com.walmart.feeds.api.resources.feed.response.FeedResponse;
import com.walmart.feeds.api.resources.feed.validator.annotation.ValidSlug;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@RequestMapping(FeedsController.V1_FEEDS)
public class FeedsController {

    public static final String V1_FEEDS = "/v1/partners/{partnerSlug}/feeds";

    @Autowired
    private FeedService feedService;

    @ApiOperation(value = "Create a new feed",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful feed creation", response = ResponseEntity.class),
            @ApiResponse(code = 409, message = "FeedEntity already exists"),
            @ApiResponse(code = 404, message = "Invalid partner")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createFeed(@Valid @RequestBody FeedRequest request, @PathVariable("partnerSlug") String partnerSlug, UriComponentsBuilder builder) {

        FeedEntity feedEntity = FeedEntity.builder()
                .slug(SlugParserUtil.toSlug(request.getName()))
                .name(request.getName())
                .notificationFormat(FeedNotificationFormat.getFromCode(request.getNotification().getFormat()))
                .notificationMethod(FeedNotificationMethod.getFromCode(request.getNotification().getMethod()))
                .notificationUrl(request.getNotification().getUrl())
                .active(request.getActive())
                .collectionId(request.getCollectionId())
                .partnerTaxonomy(PartnerTaxonomyEntity.builder()
                        .slug(request.getTaxonomy())
                        .build())
                .partner(PartnerEntity.builder()
                        .slug(partnerSlug)
                        .build())
                .taxonomyBlacklist(TaxonomyBlacklistEntity.builder()
                        .slug(request.getTaxonomyBlacklist())
                        .build())
                .termsBlacklist(getTermsBlacklist(request.getTermsBlacklist()))
                .type(FeedType.getFromCode(request.getType()))
                .cronPattern(request.getCronPattern())
                .template(TemplateEntity.builder()
                        .slug(request.getTemplate())
                        .build())
                .utms(request.getUtms())
                .build();

        FeedEntity savedFeedEntity = feedService.createFeed(feedEntity);

        UriComponents uriComponents = builder.path(V1_FEEDS.concat("/{slug}")).buildAndExpand(partnerSlug, savedFeedEntity.getSlug());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }

    @ApiOperation(value = "Fetch feed by slug", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return found feed", response = FeedResponse.class),
            @ApiResponse(code = 404, message = "FeedEntity not found by slug")})
    @RequestMapping(value = "{feedSlug}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchFeed(@ValidSlug @PathVariable("partnerSlug") String partnerSlug, @ValidSlug @PathVariable("feedSlug") String feedSlug) {

        FeedEntity feedEntity = feedService.fetchByPartner(feedSlug, partnerSlug);

        return ResponseEntity.ok().body(FeedResponse.builder()
                .name(feedEntity.getName())
                .template(feedEntity.getTemplate().getSlug())
                .taxonomy(feedEntity.getPartnerTaxonomy() != null ? feedEntity.getPartnerTaxonomy().getSlug() : null)
                .taxonomyBlacklist(getTaxonomyBlacklistSlug(feedEntity))
                .termsBlacklist(feedEntity.getTermsBlacklist().stream().map(TermsBlacklistEntity::getSlug).collect(Collectors.toList()))
                .slug(feedEntity.getSlug())
                .collectionId(feedEntity.getCollectionId())
                .notification(FeedNotificationData.builder()
                        .format(feedEntity.getNotificationFormat().getType())
                        .method(feedEntity.getNotificationMethod().getType())
                        .url(feedEntity.getNotificationUrl())
                        .build())
                .partner(PartnerResponse.builder()
                        .slug(feedEntity.getPartner().getSlug())
                        .active(feedEntity.getPartner().isActive())
                        .description(feedEntity.getPartner().getDescription())
                        .name(feedEntity.getPartner().getName())
                        .creationDate(feedEntity.getPartner().getCreationDate())
                        .updateDate(feedEntity.getPartner().getUpdateDate())
                        .partnerships(feedEntity.getPartner().getPartnershipsAsList())
                        .build())
                .type(feedEntity.getType())
                .utms(feedEntity.getUtms())
                .creationDate(feedEntity.getCreationDate())
                .updateDate(feedEntity.getUpdateDate())
                .cronPattern(feedEntity.getCronPattern())
                .active(feedEntity.isActive())
                .build());
    }

    @ApiOperation(value = " Fetch all feeds by partner slug ",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return found feeds", response = FeedResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Invalid partner slug")})
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionResponse<FeedResponse>> fetchAll(@ValidSlug @PathVariable("partnerSlug") String partnerSlug, @RequestParam(value = "active", required = false) Boolean active) {

        List<FeedEntity> listFeedEntity = feedService.fetchActiveByPartner(partnerSlug, active);

        return ResponseEntity.ok().body(CollectionResponse.<FeedResponse>builder()
                .result(listFeedEntity.stream().map(f ->
                        FeedResponse.builder()
                                .name(f.getName())
                                .slug(f.getSlug())
                                .template(f.getTemplate().getSlug())
                                .taxonomy(f.getPartnerTaxonomy() != null ? f.getPartnerTaxonomy().getSlug() : null)
                                .taxonomyBlacklist(getTaxonomyBlacklistSlug(f))
                                .termsBlacklist(f.getTermsBlacklist().stream().map(TermsBlacklistEntity::getSlug).collect(Collectors.toList()))
                                .notification(FeedNotificationData.builder()
                                        .format(f.getNotificationFormat().getType())
                                        .method(f.getNotificationMethod().getType())
                                        .url(f.getNotificationUrl())
                                        .build())
                                .partner(PartnerResponse.builder()
                                        .slug(f.getPartner().getSlug())
                                        .active(f.getPartner().isActive())
                                        .description(f.getPartner().getDescription())
                                        .name(f.getPartner().getName())
                                        .creationDate(f.getPartner().getCreationDate())
                                        .updateDate(f.getPartner().getUpdateDate())
                                        .partnerships(f.getPartner().getPartnershipsAsList())
                                        .build())
                                .type(f.getType())
                                .utms(f.getUtms())
                                .creationDate(f.getCreationDate())
                                .updateDate(f.getUpdateDate())
                                .active(f.isActive())
                                .cronPattern(f.getCronPattern())
                                .collectionId(f.getCollectionId())
                                .build()).collect(Collectors.toList())).build());
    }

    @ApiOperation(value = "Change feed status",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Status modified with success", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Invalid feed or partner slug")})
    @RequestMapping(value = "{feedSlug}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeFeedStatus(@ValidSlug @PathVariable("partnerSlug") String partnerSlug, @ValidSlug @PathVariable("feedSlug") String feedSlug, @RequestParam(value = "active", required = true) Boolean active) {

        feedService.changeFeedStatus(partnerSlug, feedSlug, active);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update feed",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "FeedEntity removed with success", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Invalid feed reference")})
    @RequestMapping(value = "{feedSlug}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateFeed(@ValidSlug @RequestBody FeedRequest request, @ValidSlug @PathVariable("feedSlug") String feedSlug, @ValidSlug @PathVariable("partnerSlug") String partnerSlug) {

        FeedEntity feedEntity = FeedEntity.builder()
                .slug(feedSlug)
                .name(request.getName())
                .notificationFormat(FeedNotificationFormat.getFromCode(request.getNotification().getFormat()))
                .notificationMethod(FeedNotificationMethod.getFromCode(request.getNotification().getMethod()))
                .notificationUrl(request.getNotification().getUrl())
                .template(TemplateEntity.builder()
                        .slug(request.getTemplate())
                        .build())
                .partnerTaxonomy(PartnerTaxonomyEntity.builder()
                        .slug(request.getTaxonomy())
                        .build())
                .active(request.getActive())
                .collectionId(request.getCollectionId())
                .partner(PartnerEntity.builder()
                        .slug(partnerSlug)
                        .build())
                .taxonomyBlacklist(TaxonomyBlacklistEntity.builder()
                        .slug(request.getTaxonomyBlacklist())
                        .build())
                .termsBlacklist(getTermsBlacklist(request.getTermsBlacklist()))
                .type(FeedType.getFromCode(request.getType()))
                .utms(request.getUtms())
                .cronPattern(request.getCronPattern())
                .build();

        feedService.updateFeed(feedEntity);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Validate a Feed to generate a file", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Valid Feed", response = FeedResponse.class),
            @ApiResponse(code = 400, message = "Invalid Feed")})
    @RequestMapping(value = "{feedSlug}/validation", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity feedValidation(@ValidSlug @PathVariable("partnerSlug") String partnerSlug, @ValidSlug @PathVariable("feedSlug") String feedSlug) {
        feedService.validateFeed(partnerSlug, feedSlug);
        return ResponseEntity.ok().build();
    }

    private String getTaxonomyBlacklistSlug(FeedEntity feedEntity) {
        if (feedEntity.getTaxonomyBlacklist() == null) {
            return null;
        }
        return feedEntity.getTaxonomyBlacklist().getSlug();
    }

    private List<TermsBlacklistEntity> getTermsBlacklist(List<String> slugs) {

        List<TermsBlacklistEntity> termsBlacklistEntities = new ArrayList<>();

        if (slugs == null || slugs.isEmpty()) {
            return termsBlacklistEntities;
        }

        slugs.forEach(slug -> termsBlacklistEntities.add(feedService.findTermBlacklistBySlug(slug)));

        return termsBlacklistEntities;
    }
}
