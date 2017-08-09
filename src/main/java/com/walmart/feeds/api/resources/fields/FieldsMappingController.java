package com.walmart.feeds.api.resources.fields;

import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.core.service.feed.FeedService;
import com.walmart.feeds.api.core.service.feed.FeedServiceImpl;
import com.walmart.feeds.api.core.service.fields.FieldsMappingService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;
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

    //@Autowired
    private FeedService feedService = new FeedServiceImpl();

    @ApiOperation(value = "Create new fields mapping",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = " Successful new fields mapping ", response = FieldsMappingRequest.class),
            @ApiResponse(code = 409, message = " FieldsMappingEntity already exists "),
            @ApiResponse(code = 500, message = " Unhandled exception ")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> createFieldsMapping(@RequestBody @Valid FieldsMappingRequest fieldsMappingRequest, UriComponentsBuilder builder) {

        FieldsMappingEntity fieldsMapping;
        fieldsMapping = FieldsMappingEntity.builder()
                .mappedFields(fieldsMappingRequest.getMappedFields())
                .name(fieldsMappingRequest.getName())
                .slug(SlugParserUtil.toSlug(fieldsMappingRequest.getName()))
                .build();

        fieldsMappingService.saveFieldsdMapping(fieldsMapping);
        UriComponents uriComponents = builder.path(URI_FIELDSDMAPPING.concat("/{partnerSlug}")).buildAndExpand(fieldsMapping.getSlug());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }

    @RequestMapping(value = "{slug}", method = RequestMethod.PUT)
    public ResponseEntity updateMapping(@RequestBody @Valid FieldsMappingRequest request,
                                        @PathVariable("slug") String slug) {

        // TODO: 09/08/17 validate null elements on request.mappedfields

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

        fieldsMappingService.updateFieldsMapping(mappingEntity);

        return ResponseEntity.ok().build();
    }
}

