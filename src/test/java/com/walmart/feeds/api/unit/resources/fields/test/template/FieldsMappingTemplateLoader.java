package com.walmart.feeds.api.unit.resources.fields.test.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.request.PartnerUpdateRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldsMappingTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(MappedFieldEntity.class).addTemplate("mapped_field", new Rule() {{
            add("wmField", "Buscape");
            add("partnerField", "descricao");
            add("required", true);
        }});

        List<MappedFieldEntity> mappedFields = new ArrayList<>();
        mappedFields.add(Fixture.from(MappedFieldEntity.class).gimme("mapped_field"));
        Fixture.of(FieldsMappingRequest.class).addTemplate("valid_fields_mapping_request", new Rule() {{
            add("name", "Buscape");
            add("mappedFields", mappedFields);
        }});

        List<MappedFieldEntity> mappedFields01 = new ArrayList<>();
        mappedFields01.add(Fixture.from(MappedFieldEntity.class).gimme("mapped_field"));
        Fixture.of(FieldsMappingRequest.class).addTemplate("valid_fields_mapping_notblank", new Rule() {{
            add("name", "");
            add("mappedFields", mappedFields);
        }});

        Fixture.of(FieldsMappingRequest.class).addTemplate("fields_mapping_request_empty_mapped_fields", new Rule() {{
            add("name", "Buscape");
        }});

        Fixture.of(FieldsMappingRequest.class).addTemplate("fields_mapping_request_null_mapped_fields", new Rule() {{
            add("name", "Buscape");
            add("mappedFields", Arrays.asList(null, null));
        }});

        Fixture.of(PartnerUpdateRequest.class).addTemplate("invalid_fields_mapping_udpate_request", new Rule() {{
            add("name", "Buscape");
            add("mappedFields", Arrays.asList("price"));
        }});

        Fixture.of(PartnerEntity.class).addTemplate("fields_mapping_entity", new Rule() {{
            add("name", "Buscape");
            add("mappedFields", Arrays.asList("price"));
        }});
    }
}
