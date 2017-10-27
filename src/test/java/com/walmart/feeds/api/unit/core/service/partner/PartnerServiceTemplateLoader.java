package com.walmart.feeds.api.unit.core.service.partner;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;

public class PartnerServiceTemplateLoader implements TemplateLoader {

    public static final String PARTNER_ENTITY = "partner-entity";
    public static final String PARTNER_ENTITY_PARTNERSHIPS_NULL = "partner-ships-null";

    @Override
    public void load() {

        Fixture.of(PartnerEntity.class).addTemplate(PARTNER_ENTITY, new Rule() {{

            add("name", "Facebook-terms-blacklist");
            add("slug", "facebook-terms-blacklist");
            add("description", "New partner");
            add("partnerships", "Test123");
        }});

        Fixture.of(PartnerEntity.class).addTemplate(PARTNER_ENTITY_PARTNERSHIPS_NULL, new Rule() {{

            add("name", "PartnerEntity");
            add("slug", "partnerentity");
            add("description", "New partner");
            add("partnerships", "");
        }});
    }
}
