package com.walmart.feeds.api.resources.blacklist;


import com.walmart.feeds.api.resources.blacklist.request.TaxonomyBlacklistRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY)
public class TaxonomyBlackListController {

    public static final String V1_BLACKLIST_TAXONOMY = "/v1/blacklist/taxonomies";

    @ApiOperation(value = "Create a taxonomy blacklist",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful taxonomy blacklist creation", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Validation error")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTaxonomyBlacklist(@Valid @RequestBody TaxonomyBlacklistRequest taxonomyBlacklistRequest, UriComponentsBuilder builder){
        return null;
    }

    @ApiOperation(value = "Fetch a list or a single taxonomy blacklist",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful taxonomy blacklist fetch", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Taxonomy blacklist not found")})
    @RequestMapping(value = "{taxonomyBlacklistSlug}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetchTaxonomyBlackList(@PathVariable(value = "taxonomyBlacklistSlug", required = false) String slug, UriComponentsBuilder builder){
        return null;
    }

    @ApiOperation(value = "Update a taxonomy blacklist",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful taxonomy blacklist update", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Validation error"),
            @ApiResponse(code = 404, message = "Taxonomy blacklist not found")})
    @RequestMapping(value = "{taxonomyBlacklistSlug}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTaxonomyBlackList(@Valid @RequestBody TaxonomyBlacklistRequest taxonomyBlacklistRequest,
                                                  @PathVariable(value = "taxonomyBlacklistSlug") String slug,
                                                  UriComponentsBuilder builder){
        return null;
    }

    @ApiOperation(value = "Delete a taxonomy blacklist",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful taxonomy blacklist delete", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Taxonomy blacklist not found")})
    @RequestMapping(value = "{taxonomyBlacklistSlug}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteTaxonomyBlackList(@PathVariable(value = "taxonomyBlacklistSlug") String slug, UriComponentsBuilder builder){
        return null;
    }
}
