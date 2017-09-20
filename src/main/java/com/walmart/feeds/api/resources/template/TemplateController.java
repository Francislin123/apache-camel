package com.walmart.feeds.api.resources.template;

import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.template.TemplateService;
import com.walmart.feeds.api.resources.template.response.TemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("{slug}")
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
