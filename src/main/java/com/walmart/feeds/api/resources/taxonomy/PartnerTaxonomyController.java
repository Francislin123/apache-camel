package com.walmart.feeds.api.resources.taxonomy;

import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.core.utils.TaxonomyMappingCSVHandler;
import com.walmart.feeds.api.resources.taxonomy.request.UploadTaxonomyMappingTO;
import com.walmart.feeds.api.resources.taxonomy.request.UploadTaxonomyRequest;
import com.walmart.feeds.api.resources.taxonomy.response.PartnerTaxonomyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@RequestMapping(PartnerTaxonomyController.V1_PARTNER_TAXONOMY)
public class PartnerTaxonomyController {

    public static final String V1_PARTNER_TAXONOMY = "/v1/partners/{partnerSlug}/taxonomy";

    public static final String TEXT_CSV = "text/csv";

    @Autowired
    private PartnerTaxonomyService partnerTaxonomyService;

    @ApiOperation(value = "Upload a taxonomy file",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Request to import taxonomy file was accepted and will be executed asynchronously", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "File not found")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadTaxonomyMappingFile(@PathVariable("partnerSlug") String partnerSlug, @Valid @ModelAttribute UploadTaxonomyRequest uploadTaxonomyRequest, UriComponentsBuilder builder) throws IOException {

        String taxonomySlug = SlugParserUtil.toSlug(uploadTaxonomyRequest.getName());

        partnerTaxonomyService.processFile(UploadTaxonomyMappingTO.builder()
                .slug(taxonomySlug)
                .name(uploadTaxonomyRequest.getName())
                .partnerSlug(partnerSlug)
                .taxonomyMapping(uploadTaxonomyRequest.getFile())
            .build());

        UriComponents uriComponents = builder.path(V1_PARTNER_TAXONOMY.concat("/{slug}")).buildAndExpand(partnerSlug, taxonomySlug);

        return ResponseEntity.accepted().header(HttpHeaders.LOCATION, uriComponents.toUriString()).build();
    }


    @ApiOperation(value = "Delete a taxonomy mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successful delete", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Partner Taxonomy not found")})
    @RequestMapping(value = "{taxonomySlug}", method = RequestMethod.DELETE)
    public ResponseEntity removeBySlug(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("taxonomySlug") String taxonomySlug) {

        partnerTaxonomyService.removeEntityBySlug(partnerSlug, taxonomySlug);

        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Fetch a taxonomy mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful fetch", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Partner Taxonomy or partner not found")})
    @RequestMapping(value = {"", "{taxonomySlug}"}, method = RequestMethod.GET)
    public ResponseEntity fetchBySlug(@PathVariable("partnerSlug") String partnerSlug, @PathVariable(value = "taxonomySlug", required = false) String taxonomySlug, UriComponentsBuilder builder) {

        List<PartnerTaxonomyEntity> entities = partnerTaxonomyService.fetchPartnerTaxonomies(partnerSlug, taxonomySlug);
        List<PartnerTaxonomyResponse> response = entities.stream().map(t -> {
            UriComponentsBuilder uriComponentsBuilder = builder.cloneBuilder();
            return PartnerTaxonomyResponse.builder()
                        .archiveName(t.getFileName())
                        .status(t.getStatus())
                        .slug(t.getSlug())
                        .mappingDate(t.getCreationDate())
                        .link(uriComponentsBuilder.path(V1_PARTNER_TAXONOMY.concat("/download/{slug}")).buildAndExpand(t.getPartner().getSlug(), t.getSlug()).toUriString())
                    .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "Download a taxonomy mapping file",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful download", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Partner Taxonomy or partner not found")})
    @RequestMapping(value = "download/{taxonomySlug}", method = RequestMethod.GET)
    public ResponseEntity downloadCSVFile(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("taxonomySlug") String taxonomySlug, UriComponentsBuilder builder, HttpServletResponse response) throws IOException {
        PartnerTaxonomyEntity entity = partnerTaxonomyService.fetchProcessedPartnerTaxonomy(partnerSlug, taxonomySlug);

        response.setContentType(TEXT_CSV);

        StringBuilder sb = TaxonomyMappingCSVHandler.createGenericHeader();
        TaxonomyMappingCSVHandler.returnFileBody(entity, sb);

        response.getOutputStream().write(sb.toString().getBytes());

        return ResponseEntity.ok().build();
    }

}
