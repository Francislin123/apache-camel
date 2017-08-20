package com.walmart.feeds.api.resources.taxonomy;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import com.walmart.feeds.api.core.utils.TaxonomyMappingCSVHandler;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.taxonomy.response.PartnerTaxonomyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
            @ApiResponse(code = 201, message = "Successful file upload", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "File not found")})
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadTaxonomyMappingFile(@PathVariable("partnerSlug") String partnerSlug, @RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder builder) throws URISyntaxException, IOException {

        partnerTaxonomyService.processFile(partnerSlug, multipartFile);

        UriComponents uriComponents = builder.path(V1_PARTNER_TAXONOMY.concat("/{slug}")).buildAndExpand(partnerSlug, SlugParserUtil.toSlug(FilenameUtils.getBaseName(multipartFile.getOriginalFilename())));

        return ResponseEntity.accepted().header(HttpHeaders.LOCATION, uriComponents.toUriString()).build();
    }


    @ApiOperation(value = "Delete a taxonomy mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful delete", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Partner Taxonomy not found")})
    @RequestMapping(value = "{taxonomySlug}", method = RequestMethod.DELETE)
    public ResponseEntity removeBySlug(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("taxonomySlug") String taxonomySlug) throws URISyntaxException, IOException {

        partnerTaxonomyService.removeEntityBySlug(partnerSlug, taxonomySlug);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Fetch a taxonomy mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful fetch", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Partner Taxonomy or partner not found")})
    @RequestMapping(value = "{taxonomySlug}", method = RequestMethod.GET)
    public ResponseEntity fetchBySlug(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("taxonomySlug") String taxonomySlug, UriComponentsBuilder builder) {

        List<PartnerTaxonomyEntity> entities = partnerTaxonomyService.fetchPartnerTaxonomies(partnerSlug, taxonomySlug);
        List<PartnerTaxonomyResponse> response = entities.stream().map(t ->
                PartnerTaxonomyResponse.builder()
                .archiveName(t.getFileName()).slug(t.getSlug()).mappingDate(t.getCreationDate())
                .link(builder.path(V1_PARTNER_TAXONOMY.concat("/download/{slug}")).buildAndExpand(t.getPartner().getSlug(), t.getSlug()).toUriString()).build()).collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "Download a taxonomy mapping file",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful download", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Partner Taxonomy or partner not found")})
    @RequestMapping(value = "download/{taxonomySlug}", method = RequestMethod.GET)
    public @ResponseBody Resource downloadCSVFile(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("taxonomySlug") String taxonomySlug, UriComponentsBuilder builder, HttpServletResponse response) throws IOException {
        PartnerTaxonomyEntity entity = partnerTaxonomyService.fetchPartnerTaxonomy(partnerSlug, taxonomySlug);
        File file = TaxonomyMappingCSVHandler.createCSVFile(entity);
        if(null == file){
            throw new EntityNotFoundException("There's no content to create a file");
        }
        response.setContentType(TEXT_CSV);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

}
