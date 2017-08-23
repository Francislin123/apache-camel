package com.walmart.feeds.api.unit.resources.fields.test.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldsMappingTemplateLoader implements TemplateLoader {

    public static final String FIELDS_MAPPING_REQUEST = "fields_mapping_request";
    public static final String FIELDS_MAPPING_EMPTY_NAME = "fields_mapping_empty_name";
    public static final String FIELDS_MAPPING_WHITESPACES_NAME = "fields_mapping_whitespaces_name";
    public static final String FIELDS_MAPPING_REQUEST_EMPTY_MAPPED_FIELDS = "fields_mapping_request_empty_mapped_fields";
    public static final String FIELDS_MAPPING_REQUEST_NULL_MAPPED_FIELDS = "fields_mapping_request_null_mapped_fields";
    public static final String FIELDS_MAPPING_REQUEST_INVALID_MAPPED_FIELDS = "fields_mapping_request_invalid_mapped_fields";

    @Override
    public void load() {

        // Mapped fields

        Fixture.of(MappedFieldEntity.class).addTemplate("mapped_field", new Rule() {{
            add("wmField", "Buscape");
            add("partnerField", "descricao");
            add("required", true);
        }});
        List<MappedFieldEntity> mappedFields = new ArrayList<>();
        mappedFields.add(Fixture.from(MappedFieldEntity.class).gimme("mapped_field"));

        Fixture.of(MappedFieldEntity.class).addTemplate("invalid_mapped_field", new Rule() {{
            add("wmField", null);
            add("partnerField", "    ");
        }});

        List<MappedFieldEntity> invalidMappedFields = new ArrayList<>();
        invalidMappedFields.add(Fixture.from(MappedFieldEntity.class).gimme("invalid_mapped_field"));


        // Fields Mapping


        Fixture.of(FieldsMappingRequest.class).addTemplate(FIELDS_MAPPING_REQUEST, new Rule() {{
            add("name", "Buscape");
            add("mappedFields", mappedFields);
        }});

        Fixture.of(FieldsMappingRequest.class).addTemplate(FIELDS_MAPPING_EMPTY_NAME, new Rule() {{
            add("name", "");
            add("mappedFields", mappedFields);
        }});

        Fixture.of(FieldsMappingRequest.class).addTemplate(FIELDS_MAPPING_WHITESPACES_NAME, new Rule() {{
            add("name", "     ");
            add("mappedFields", mappedFields);
        }});

        Fixture.of(FieldsMappingRequest.class).addTemplate(FIELDS_MAPPING_REQUEST_EMPTY_MAPPED_FIELDS, new Rule() {{
            add("name", "Buscape");
        }});

        Fixture.of(FieldsMappingRequest.class).addTemplate(FIELDS_MAPPING_REQUEST_NULL_MAPPED_FIELDS, new Rule() {{
            add("name", "Buscape");
            add("mappedFields", Arrays.asList(null, null));
        }});


        Fixture.of(FieldsMappingRequest.class).addTemplate(FIELDS_MAPPING_REQUEST_INVALID_MAPPED_FIELDS, new Rule() {{
            add("name", "Buscape");
            add("mappedFields", invalidMappedFields);
        }});

    }
}
