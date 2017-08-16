package com.walmart.feeds.api.resources.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureService;
import com.walmart.feeds.api.resources.feed.CollectionResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Api
@RestController
@RequestMapping(CommercialStructureController.V1_COMMERCIAL_STRUCTURE)
public class CommercialStructureController {

    public static final String V1_COMMERCIAL_STRUCTURE = "/v1/partners/{partnerSlug}/commercialstructure";

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private CommercialStructureService commercialStructureService;

    @ApiOperation(value = "Upload a commercial structure file",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful file upload", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "File not found")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadCommercialStructure(@PathVariable("partnerSlug") String partnerSlug, @RequestParam("file") MultipartFile multipartFile) throws URISyntaxException, IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("partnerSlug", partnerSlug);
        map.put("archiveName", multipartFile.getOriginalFilename());
        producerTemplate.sendBodyAndHeaders("direct:loadCsFile", multipartFile.getInputStream(), map);

        //TODO URI COMPONENT FAILING TO PASS THROUGH
        return ResponseEntity.created(new URI("http:localhost")).build();
    }


    @ApiOperation(value = "Delete a commercial structure mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful file upload", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Commercial Structure not found")})
    @RequestMapping(value = "{csSlug}", method = RequestMethod.DELETE)
    public ResponseEntity removeBySlug(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("csSlug") String csSlug) throws URISyntaxException, IOException {

        commercialStructureService.removeEntityBySlug(partnerSlug, csSlug);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete a commercial structure mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful file upload", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Commercial Structure not found")})
    @RequestMapping(value = "{csSlug}", method = RequestMethod.GET)
    public ResponseEntity fetchBySlug(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("csSlug") String csSlug,
                                      @RequestParam("page") int page, @RequestParam("size") int size) {

        Page<CommercialStructureEntity> resultPage = commercialStructureService.fetchBySlug(partnerSlug, csSlug, page, size);

        return ResponseEntity.ok().build();
    }

}
