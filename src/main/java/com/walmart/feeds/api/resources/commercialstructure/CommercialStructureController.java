package com.walmart.feeds.api.resources.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureService;
import com.walmart.feeds.api.core.utils.CommercialStructureCSVHandler;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.commercialstructure.response.CommercialStructureResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api
@RestController
@RequestMapping(CommercialStructureController.V1_COMMERCIAL_STRUCTURE)
public class CommercialStructureController {

    public static final String V1_COMMERCIAL_STRUCTURE = "/v1/partners/{partnerSlug}/commercialstructure";

    public static final String TEXT_CSV = "text/csv";

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
    public ResponseEntity uploadCommercialStructure(@PathVariable("partnerSlug") String partnerSlug, @RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder builder) throws URISyntaxException, IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("partnerSlug", partnerSlug);
        map.put("archiveName", FilenameUtils.getBaseName(multipartFile.getOriginalFilename()));
        producerTemplate.sendBodyAndHeaders("direct:loadCsFile", multipartFile.getInputStream(), map);

        //TODO URI COMPONENT FAILING TO PASS THROUGH

        UriComponents uriComponents = builder.path(V1_COMMERCIAL_STRUCTURE.concat("/{slug}")).buildAndExpand(partnerSlug, SlugParserUtil.toSlug(FilenameUtils.getBaseName(multipartFile.getOriginalFilename())));
        return ResponseEntity.created(uriComponents.toUri()).build();
    }


    @ApiOperation(value = "Delete a commercial structure mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful delete", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Commercial Structure not found")})
    @RequestMapping(value = "{csSlug}", method = RequestMethod.DELETE)
    public ResponseEntity removeBySlug(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("csSlug") String csSlug) throws URISyntaxException, IOException {

        commercialStructureService.removeEntityBySlug(partnerSlug, csSlug);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Fetch a commercial structure mapping by slug",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful fetch", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Commercial Structure or partner not found")})
    @RequestMapping(value = "{csSlug}", method = RequestMethod.GET)
    public ResponseEntity fetchBySlug(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("csSlug") String csSlug, UriComponentsBuilder builder) {

        List<CommercialStructureEntity> entities = commercialStructureService.fetchCommercialStructure(partnerSlug, csSlug);
        List<CommercialStructureResponse> response = entities.stream().map(cs ->
                CommercialStructureResponse.builder()
                .archiveName(cs.getArchiveName()).slug(cs.getSlug()).mappingDate(cs.getCreationDate())
                .link(builder.path(V1_COMMERCIAL_STRUCTURE.concat("/download/{slug}")).buildAndExpand(cs.getPartner().getSlug(), cs.getSlug()).toUriString()).build()).collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "Download a commercial structure file",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful download", response = ResponseEntity.class),
            @ApiResponse(code = 404, message = "Commercial Structure or partner not found")})
    @RequestMapping(value = "download/{csSlug}", method = RequestMethod.GET)
    public @ResponseBody Resource downloadCSVFile(@PathVariable("partnerSlug") String partnerSlug, @PathVariable("csSlug") String csSlug, UriComponentsBuilder builder, HttpServletResponse response) throws IOException {
        CommercialStructureEntity entity = commercialStructureService.fetchOneCommercialStructure(partnerSlug, csSlug);
        File file = CommercialStructureCSVHandler.createCSVFile(entity);

        response.setContentType(TEXT_CSV);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        return new FileSystemResource(file);
    }

}
