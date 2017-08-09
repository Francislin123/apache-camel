package com.walmart.feeds.api.resources.fields;

import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.core.service.fields.FieldsMappingService;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping(FieldsMappingController.V1_FIELDS_MAPPING)
public class FieldsMappingController {

    public static final String V1_FIELDS_MAPPING = "/v1/fieldsmapping";

    @Autowired
    private FieldsMappingService fieldsMappingService;

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
