package com.walmart.feeds.api.resources.commercialstructure;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.resources.commercialstructure.service.CommercialStructureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vn0y942 on 07/08/17.
 */
@Api
@RestController
@RequestMapping(CommercialStructureController.V1_COMMERCIAL_STRUCTURE)
public class CommercialStructureController {

    public static final String V1_COMMERCIAL_STRUCTURE = "/v1/partners/{partnerSlug}/feeds/{feedSlug}/cs";

    @Autowired
    private CommercialStructureService commercialStructureService;

    @Autowired
    private ProducerTemplate producerTemplate;

    @ApiOperation(value = "Upload a commercial structure file",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful file upload", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "File not found")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadCommercialStructure(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("feedSlug") String feedSlug, @RequestParam("file") MultipartFile multipartFile) throws NotFoundException, URISyntaxException, IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("partnerSlug", partnerSlug);
        map.put("archiveName", multipartFile.getName());
        producerTemplate.requestBodyAndHeaders("direct:test", multipartFile.getInputStream(), map);

        //TODO URI COMPONENT FAILING TO PASS THROUGH
        return ResponseEntity.created(new URI("http:localhost")).build();
    }
}
