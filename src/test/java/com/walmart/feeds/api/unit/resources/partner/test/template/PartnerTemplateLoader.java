package com.walmart.feeds.api.unit.resources.partner.test.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.request.PartnerUpdateRequest;

import java.util.Arrays;

public class PartnerTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(PartnerRequest.class).addTemplate("valid_partner_request", new Rule() {{
            add("name", "Buscape");
            add("reference", "buscape");
            add("description", "Texto descritivo");
            add("partnerships", Arrays.asList("comparadores", "big"));
        }});

        Fixture.of(PartnerRequest.class).addTemplate("invalid_partner_request_no_partnerships", new Rule() {{
            add("name", "Buscape");
            add("reference", "buscape");
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

        Fixture.of(PartnerTO.class).addTemplate("partner_to", new Rule() {{
            add("name", "Buscape");
            add("reference", "buscape");
            add("description", "Texto descritivo");
            add("partnerships", Arrays.asList("comparadores", "big"));
            add("active", true);
        }});

    }
}
