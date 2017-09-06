package com.walmart.feeds.api.unit.camel.test.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.camel.TaxonomyMappingBindy;

public class TaxonomyBindyTemplateLoader implements TemplateLoader {


    public static final String VALID_TAXONOMY_BINDY = "valid-taxonomy-bindy";
    public static final String BINDY_WITHOUT_WALMART_TAXONOMY = "bindy-without-walmart-taxonomy";
    public static final String BINDY_WITHOUT_ID = "bindy-without-id";
    public static final String BINDY_WITHOUT_PARTNER_TAXONOMY = "bindy-without-partner-taxonomy";

    @Override
    public void load() {
        Fixture.of(TaxonomyMappingBindy.class).addTemplate(VALID_TAXONOMY_BINDY, new Rule() {{
            add("structurePartnerId", "teste123");
            add("partnerTaxonomy", "partner > category > teste");
            add("walmartTaxonomy", "walmart > category > subcategory");
        }});

        Fixture.of(TaxonomyMappingBindy.class).addTemplate(BINDY_WITHOUT_WALMART_TAXONOMY).inherits("valid-taxonomy-bindy", new Rule() {{
            add("walmartTaxonomy", null);
        }});

        Fixture.of(TaxonomyMappingBindy.class).addTemplate(BINDY_WITHOUT_ID).inherits("valid-taxonomy-bindy", new Rule() {{
            add("structurePartnerId", null);
        }});

        Fixture.of(TaxonomyMappingBindy.class).addTemplate(BINDY_WITHOUT_PARTNER_TAXONOMY).inherits("valid-taxonomy-bindy", new Rule() {{
            add("partnerTaxonomy", null);
        }});
    }
}
