package com.walmart.feeds.api.resources.partner;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.feed.CollectionResponse;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.request.PartnerUpdateRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponseList;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(PartnerController.V1_PARTNERS)
public class PartnerController {

    public static final String V1_PARTNERS = "/v1/partners";

    private Logger logger = LoggerFactory.getLogger(PartnerController.class);

    @Autowired
    private PartnerService service;

    @ApiOperation(value = "Create new partner",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = " Successful new partner ", response = PartnerRequest.class),
            @ApiResponse(code = 409, message = " PartnerEntity already exists "),
            @ApiResponse(code = 500, message = " Unhandled exception ")})
    @RequestMapping(method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createPartner(@RequestBody @Valid PartnerRequest partnerRequest, UriComponentsBuilder builder) {

            PartnerEntity partner = PartnerEntity.builder()
                    .slug(SlugParserUtil.toSlug(partnerRequest.getName()))
                    // TODO[r0i001q]: Export to Utility class
                    .partnerships(String.join(";", partnerRequest.getPartnership()))
                    .name(partnerRequest.getName())
                    .description(partnerRequest.getDescription())
                    .active(partnerRequest.isActive())
                    .build();service.savePartner(partner);

    UriComponents uriComponents =
                    builder.path(V1_PARTNERS.concat("/{partnerSlug}")).buildAndExpand(partner.getSlug());        return ResponseEntity.created(uriComponents.toUri()).build();

    }

    @ApiOperation(value = " Method to find the partner by your slug ",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " PartnerEntity found  ", response = PartnerResponse.class),
            @ApiResponse(code = 404, message = " PartnerEntity not found ")})
	@RequestMapping(value = "/{slug}",
            method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity fetchPartnerBySlug(@PathVariable("slug") String slug) throws NotFoundException {

            PartnerEntity partner = service.findBySlug(slug);

            return ResponseEntity.ok(
                    PartnerResponse.builder()
                            .slug(partner.getSlug())
                            .name(partner.getName())
                            .description(partner.getDescription())
                            .partnerships(partner.getPartnershipsAsList())
                            .active(partner.isActive())
                            .creationDate(partner.getCreationDate())
                            .updateDate(partner.getUpdateDate())
                            .build());

    }

    @ApiOperation(value = " Method to change partner fields ",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " PartnerEntity change successfully ", response = PartnerRequest.class),
            @ApiResponse(code = 404, message = " PartnerEntity not change "),
            @ApiResponse(code = 500, message = " Internal Server Error ")})
    @RequestMapping(value = "/{slug}",
            method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> updatePartner(@PathVariable("slug") String slug, @RequestBody PartnerRequest partnerRequest) {
        logger.info("Updating partner slug by {}", slug);


        PartnerEntity partner = PartnerEntity.builder()
                .slug(SlugParserUtil.toSlug(partnerRequest.getName()))
                .partnerships(String.join(";", partnerRequest.getPartnerships()))
                .name(partnerRequest.getName())
                .description(partnerRequest.getDescription())
                .active(partnerRequest.isActive())
                .build();

            service.updatePartner(partner);

            service.updatePartner(partner);
            return ResponseEntity.ok().build();

	}

    @ApiOperation(value = "Method to modify partner status", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Partner status changed"),
            @ApiResponse(code = 500, message = "Failed to change partner status")})
    @RequestMapping(value = "/{slug}",
            method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> changePartnerStatus(@PathVariable("slug") String slug,
                                                 @RequestParam("active") Boolean active) {

            service.setPartnerStatus(slug, active);
            return ResponseEntity.noContent().build();

    }

    // TODO[r0i001q]: Verify content-type
    @ApiOperation(value = "Method to find the active partners", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " Partners found successfully ", response = PartnerResponseList.class),
            @ApiResponse(code = 500, message = " Internal Server Error ")})
    @RequestMapping(value = "/actives", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CollectionResponse<PartnerResponse>> fetchPartnerActives() {
        List<PartnerEntity> activePartners = service.findActivePartners();
        return ResponseEntity.ok().body(CollectionResponse.<PartnerResponse>builder()
                .result(activePartners.stream().map(p -> PartnerResponse.builder()
                        .slug(p.getSlug())
                        .updateDate(p.getUpdateDate())
                        .creationDate(p.getCreationDate())
                        .active(p.isActive())
                        .description(p.getDescription())
                        .name(p.getName())
                        .partnerships(p.getPartnershipsAsList())
                        .build()).collect(Collectors.toList())
                ).build());
    }

    @ApiOperation(value = " PartnerEntity Listing Method ",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all partners", response = CollectionResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Partner not change"),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CollectionResponse<PartnerResponse>> fetchAllPartners() {
        try {
            List<PartnerEntity> allPartners = service.findAllPartners();

            return ResponseEntity.ok().body(CollectionResponse.<PartnerResponse>builder()
                    .result(allPartners.stream().map(p -> PartnerResponse.builder()
                            .slug(p.getSlug())
                            .updateDate(p.getUpdateDate())
                            .creationDate(p.getCreationDate())
                            .active(p.isActive())
                            .description(p.getDescription())
                            .name(p.getName())
                            .partnerships(p.getPartnershipsAsList())
                            .build()).collect(Collectors.toList())
                    ).build());
    }

    @ApiOperation(value = "Method for fetching partners using query text", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = " Search found partners ", response = PartnerResponseList.class,responseContainer = "List"),
            @ApiResponse(code = 404, message = " Empty result ")})
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public ResponseEntity<CollectionResponse<PartnerResponse>> searchPartners(@RequestParam("q") String query) {
        logger.info("Searching partners using query text = {}", query);

        List<PartnerEntity> partnerResponses = this.service.searchPartners(query);

        if (partnerResponses.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(CollectionResponse.<PartnerResponse>builder()
                .result(partnerResponses.stream().map(p -> PartnerResponse.builder()
                        .slug(p.getSlug())
                        .updateDate(p.getUpdateDate())
                        .creationDate(p.getCreationDate())
                        .active(p.isActive())
                        .description(p.getDescription())
                        .name(p.getName())
                        .partnerships(p.getPartnershipsAsList())
                        .build()).collect(Collectors.toList())
                ).build());
    }

}
