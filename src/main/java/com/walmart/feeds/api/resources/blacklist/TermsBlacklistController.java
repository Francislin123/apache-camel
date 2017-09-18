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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping(TermsBlacklistController.V1_BLACKLIST_TERMS)
public class TermsBlacklistController {

    public static final String V1_BLACKLIST_TERMS = "/v1/blacklist/terms";

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

        UriComponents uriComponents = builder.path(V1_BLACKLIST_TERMS.concat("/{slug}")).buildAndExpand(termsBlacklistEntity.getSlug());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}







