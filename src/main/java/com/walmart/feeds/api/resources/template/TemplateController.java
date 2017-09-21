package com.walmart.feeds.api.resources.template;

import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.template.TemplateService;
import com.walmart.feeds.api.resources.template.response.TemplateResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TemplateController.V1_TEMPLATE)
public class TemplateController {

    public static final String V1_TEMPLATE = "/v1/templates";

    @Autowired
    private TemplateService templateService;

    @ApiOperation(value = "Fetch a template slug",
            httpMethod = "GET",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful fetch"),
            @ApiResponse(code = 404, message = "Template not found")})
    @RequestMapping(value = "{slug}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TemplateResponse> fetchBySlug(@PathVariable("slug") String slug) {
        TemplateEntity templateEntity = templateService.fetchBySlug(slug);
        TemplateResponse response = TemplateResponse.builder()
                .name(templateEntity.getName())
                .slug(templateEntity.getSlug())
                .separator(templateEntity.getSeparator())
                .header(templateEntity.getHeader())
                .body(templateEntity.getBody())
                .footer(templateEntity.getFooter())
                .format(templateEntity.getFormat())
                .build();

        return ResponseEntity.ok(response);
    }

}
