package com.walmart.feeds.api.resources.blacklist;

import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TermsBlacklistService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.blacklist.request.TermsBlacklistRequest;
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

@Api
@RestController
@RequestMapping(TermsBlacklistController.URI_TERMS_BLACKLIST)
public class TermsBlacklistController {

    public static final String URI_TERMS_BLACKLIST = "/v1/blacklist/terms";

    @Autowired
    private TermsBlacklistService termsBlacklistService;


    @ApiOperation(value = "Create a terms blacklist",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful terms blacklist creation", response = ResponseEntity.class),
            @ApiResponse(code = 409, message = "Validation error"),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTermsBlacklist(@Valid @RequestBody TermsBlacklistRequest termsBlacklistRequest, UriComponentsBuilder builder) {

        TermsBlacklistEntity termsBlacklistEntity = TermsBlacklistEntity.builder()
                .slug(SlugParserUtil.toSlug(termsBlacklistRequest.getName()))
                .name(termsBlacklistRequest.getName())
                .list(termsBlacklistRequest.getList()).build();

        termsBlacklistService.saveTermsBlacklist(termsBlacklistEntity);

        UriComponents uriComponents = builder.path(URI_TERMS_BLACKLIST.concat("/{slug}")).buildAndExpand(termsBlacklistEntity.getSlug());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @ApiOperation(value = "Update a terms blacklist",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful terms blacklist updateTermsBlacklist", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Terms blacklist not found"),
            @ApiResponse(code = 500, message = "Unhandled error terms blacklist updateTermsBlacklist")})
    @RequestMapping(value = "{termsBlacklistSlug}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTermsBlackList(@Valid @RequestBody TermsBlacklistRequest termsBlacklistRequest,
                                               @PathVariable(value = "termsBlacklistSlug") String termsBlacklistSlug, UriComponentsBuilder builder) {

        TermsBlacklistEntity blacklistEntity = TermsBlacklistEntity.builder()
                .slug(termsBlacklistSlug)
                .name(termsBlacklistRequest.getName())
                .list(termsBlacklistRequest.getList()).build();

        termsBlacklistService.updateTermsBlacklist(blacklistEntity);

        return ResponseEntity.ok().build();
    }
}






