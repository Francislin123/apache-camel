package com.walmart.feeds.api.unit.resources.partner.test.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.request.PartnerUpdateRequest;

import java.util.Arrays;

public class PartnerTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(PartnerRequest.class).addTemplate("valid_partner_request", new Rule() {{
            add("name", "Buscape");
            add("description", "Texto descritivo");
            add("partnerships", Arrays.asList("comparadores", "big"));
        }});

        Fixture.of(PartnerRequest.class).addTemplate("invalid_partner_request_no_partnerships", new Rule() {{
            add("name", "Buscape");
            add("description", "Texto descritivo");
        }});

        Fixture.of(PartnerUpdateRequest.class).addTemplate("partner_update_request", new Rule() {{
            add("name", "Buscape");
            add("description", "Texto descritivo");
            add("partnerships", Arrays.asList("comparadores", "big"));
        }});

        Fixture.of(PartnerUpdateRequest.class).addTemplate("invalid_partner_udpate_request", new Rule() {{
            add("name", "Buscape");
            add("description", "Texto descritivo");
        }});

        Fixture.of(PartnerEntity.class).addTemplate("partner_entity", new Rule() {{
            add("name", "Buscape");
            add("slug", "buscape");
            add("description", "Texto descritivo");
            add("partnerships", "comparadores;big");
            add("active", true);
        }});

    }
}
