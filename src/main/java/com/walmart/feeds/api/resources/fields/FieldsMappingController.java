package com.walmart.feeds.api.resources.fields;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.core.service.fields.FieldsMappingService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.feed.CollectionResponse;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;
import com.walmart.feeds.api.resources.fields.response.FieldsMappingResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vn0gshm on 08/08/17.
 */
@Api
@RestController
@RequestMapping(FieldsMappingController.URI_FIELDSDMAPPING)
public class FieldsMappingController {

    public static final String URI_FIELDSDMAPPING = "/v1/fieldsmapping";

    private Logger logger = LoggerFactory.getLogger(FieldsMappingController.class);

    @Autowired
    private FieldsMappingService fieldsMappingService;

    @ApiOperation(value = "Create new fields mapping",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful new fields mapping"),
            @ApiResponse(code = 409, message = "Fields mapping already exists"),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createFieldsMapping(@RequestBody @Valid FieldsMappingRequest fieldsMappingRequest, UriComponentsBuilder builder) {

        FieldsMappingEntity fieldsMapping;
        fieldsMapping = FieldsMappingEntity.builder()
                .mappedFields(fieldsMappingRequest.getMappedFields())
                .name(fieldsMappingRequest.getName())
                .slug(SlugParserUtil.toSlug(fieldsMappingRequest.getName()))
                .build();

        fieldsMappingService.save(fieldsMapping);
        UriComponents uriComponents = builder.path(URI_FIELDSDMAPPING.concat("/{partnerSlug}")).buildAndExpand(fieldsMapping.getSlug());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }

    @ApiOperation(value = "Update an existent fields mapping",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful on update the fields mapping", response = FieldsMappingRequest.class),
            @ApiResponse(code = 404, message = "Fields mapping not found"),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(value = "{slug}", method = RequestMethod.PUT)
    public ResponseEntity updateMapping(@RequestBody @Valid FieldsMappingRequest request,
                                        @PathVariable("slug") String slug) {

        FieldsMappingEntity mappingEntity = FieldsMappingEntity.builder()
                .name(request.getName())
                .slug(slug)
                .mappedFields(
                        request.getMappedFields().stream().map(m -> MappedFieldEntity.builder()
                                .partnerField(m.getPartnerField())
                                .wmField(m.getWmField())
                                .required(m.isRequired())
                                .build())
                                .collect(Collectors.toList()))
                .build();

        fieldsMappingService.update(mappingEntity);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Fetch a single fields mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Find a single fields mapping by slug",
                    response = CollectionResponse.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Fields mapping found"),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(value = "{slug}", method = RequestMethod.GET, 
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<FieldsMappingResponse> findFieldsMappingBySlug(@PathVariable("slug") String slug) {

        FieldsMappingEntity fieldsMapping = fieldsMappingService.findBySlug(slug);
        FieldsMappingResponse response = FieldsMappingResponse.builder()
                .name(fieldsMapping.getName())
                .mappedFields(fieldsMapping.getMappedFields())
                .slug(slug)
                .build();

        return ResponseEntity.ok(response);
        
    }

    @ApiOperation(value = "List of all fields mapping",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all fields mapping",
                    response = CollectionResponse.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Unhandled exception")})
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CollectionResponse<FieldsMappingResponse>> listAllFieldsMapping() {

        List<FieldsMappingEntity> allFieldsMapping = fieldsMappingService.findAll();
        return ResponseEntity.ok().body(CollectionResponse.<FieldsMappingResponse>builder()
                .result(allFieldsMapping.stream().map(f -> FieldsMappingResponse.builder()
                        .name(f.getName())
                        .slug(f.getSlug())
                        .mappedFields(f.getMappedFields())
                        .build()).collect(Collectors.toList())
                ).build());
    }

    @ApiOperation(value = "Method to delete fields mapping",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Fields mapping deleted successfully"),
            @ApiResponse(code = 404, message = "Fields mapping does not deleted because it was not found."),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @RequestMapping(value = "/{slug}",
            method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity deleteFieldsMapping(@PathVariable("slug") String slug) throws EntityNotFoundException {

        fieldsMappingService.delete(slug);

        return ResponseEntity.noContent().build();
    }

}

