package com.walmart.feeds.api.resources.blacklist.response;

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
    @ApiResponses(value =  {
            @ApiResponse(code = 201, message = "Successful terms blacklist creation", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Validation error")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTermsBlacklist(@Valid @RequestBody TermsBlacklistRequest termsBlacklistRequest, UriComponentsBuilder builder) {
    }
}







