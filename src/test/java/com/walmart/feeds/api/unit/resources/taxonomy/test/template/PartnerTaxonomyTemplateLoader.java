package com.walmart.feeds.api.unit.resources.taxonomy.test.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vn0y942 on 15/08/17.
 */
public class PartnerTaxonomyTemplateLoader implements TemplateLoader {
    @Override
    public void load() {

        Fixture.of(PartnerEntity.class).addTemplate("partner_entity", new Rule() {{
            add("name", "Buscape");
            add("slug", "buscape");
            add("description", "Texto descritivo");
            add("partnerships", "comparadores;big");
            add("active", true);
        }});

        Fixture.of(TaxonomyMappingEntity.class).addTemplate("csa-entity", new Rule(){{
            add("structurePartnerId", "123123");
            add("partnerTaxonomy", "any string");
            add("walmartTaxonomy", "any string");
        }});

        Fixture.of(PartnerTaxonomyEntity.class).addTemplate("cs-input-ok", new Rule(){{
            add("archiveName", "any-name");
            add("slug", "any-slug");
            PartnerEntity partner = Fixture.from(PartnerEntity.class).gimme("partner_entity");
            add("partner", partner);

            TaxonomyMappingEntity csa = Fixture.from(TaxonomyMappingEntity.class)
                    .gimme("csa-entity");
            List<TaxonomyMappingEntity> csaList = new ArrayList<>();
            csaList.add(csa);
            add("associationEntityList", csaList);

        }});
    }
}
