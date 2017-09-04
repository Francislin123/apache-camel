package com.walmart.feeds.api.unit.resources.blacklist.taxonomy;


import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistMapping;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyOwner;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import com.walmart.feeds.api.resources.blacklist.request.TaxonomyBlacklistMappingRequest;
import com.walmart.feeds.api.resources.blacklist.request.TaxonomyBlacklistRequest;

import java.util.*;

public class TaxonomyBlacklistTemplateLoader implements TemplateLoader{

    public static final String TAXONOMY = "taxonomy";
    public static final String TAXONOMY_BLACKLIST_REQUEST = "tax-bl-request";
    public static final String TAXONOMY_BLACKLIST = "taxonomy-blacklist-entity";
    public static final String TAXONOMY_BLACKLIST_WITHOUT_PARTNER_MAPPING = "taxonomy-blacklist-without-partner-mapping";
    public static final String TB_INVALID_WALMART_TAXONOMY = "tb-invalid-walmart-taxonomy";
    public static final String TB_INVALID_PARTNER_TAXONOMY = "tb-invalid-partner-taxonomy";
    public static final String TB_INVALID_PARTNER_TAXONOMY_NULL_ELEMENT = "tb-invalid-partner-null-taxonomy";

    @Override
    public void load() {

        Fixture.of(PartnerTaxonomyEntity.class).addTemplate(TAXONOMY, new Rule(){{
            add("name", "any-name");
            add("fileName", "taxonomy-file.csv");
            add("slug", "any-slug");

            List<TaxonomyMappingEntity> mappings = Arrays.asList(
                TaxonomyMappingEntity.builder().walmartPath("Informática").partnerPath("Informática").build(),
                TaxonomyMappingEntity.builder().walmartPath("Informática > Computadores").partnerPath("Informática > Computadores").build(),
                TaxonomyMappingEntity.builder().walmartPath("Eletrônicos > TVs").partnerPath("Eletrônicos > Vídeo").build()
            );
            add("taxonomyMappings", mappings);

        }});

        Fixture.of(TaxonomyBlacklistEntity.class).addTemplate(TAXONOMY_BLACKLIST, new Rule() {{
            add("id", UUID.randomUUID());
            add("name", "any name");
            add("slug", "any-name");


            Set<TaxonomyBlacklistMapping> blacklistMappings = new HashSet(Arrays.asList(
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART).taxonomy("Eletrônicos > TVs").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Informática").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Informática > Computadores").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Eletrônicos > Vídeo").build()
            ));

            add("list", blacklistMappings);
        }});

        Fixture.of(TaxonomyBlacklistEntity.class).addTemplate(TAXONOMY_BLACKLIST_WITHOUT_PARTNER_MAPPING).inherits(TAXONOMY_BLACKLIST, new Rule() {{
            Set<TaxonomyBlacklistMapping> blacklistMappings = new HashSet(Arrays.asList(
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART).taxonomy("Eletrônicos > TVs").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART).taxonomy("Informática").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART).taxonomy("Informática > Computadores").build()
            ));

            add("list", blacklistMappings);
        }});

        Fixture.of(TaxonomyBlacklistEntity.class).addTemplate(TB_INVALID_WALMART_TAXONOMY).inherits(TAXONOMY_BLACKLIST, new Rule() {{
            Set<TaxonomyBlacklistMapping> blacklistMappings = new HashSet(Arrays.asList(
                    // this is invalid for the tests
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART).taxonomy("Eletrônicos").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART).taxonomy("Eletrônicos > TVs").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Informática > Computadores").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Eletrônicos > Vídeo").build()
            ));

            add("list", blacklistMappings);
        }});

        Fixture.of(TaxonomyBlacklistEntity.class).addTemplate(TB_INVALID_PARTNER_TAXONOMY).inherits(TAXONOMY_BLACKLIST, new Rule() {{
            Set<TaxonomyBlacklistMapping> blacklistMappings = new HashSet(Arrays.asList(
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART).taxonomy("Eletrônicos > TVs").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Informática").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Informática > Computadores").build(),
                    // this is invalid because it is not mapped on PartnerTaxonomy above
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Eletrônicos").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Eletrônicos > Vídeo").build()
            ));

            add("list", blacklistMappings);
        }});

        Fixture.of(TaxonomyBlacklistEntity.class).addTemplate(TB_INVALID_PARTNER_TAXONOMY_NULL_ELEMENT).inherits(TAXONOMY_BLACKLIST, new Rule() {{
            Set<TaxonomyBlacklistMapping> blacklistMappings = new HashSet(Arrays.asList(
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART).taxonomy("Eletrônicos > TVs").build(),
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Informática").build(),
                    null,
                    // this is invalid because it is not mapped on PartnerTaxonomy above
                    TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.PARTNER).taxonomy("Eletrônicos > Vídeo").build()
            ));

            add("list", blacklistMappings);
        }});

        Set<TaxonomyBlacklistMappingRequest> listRequest = new HashSet<>();

        Fixture.of(TaxonomyBlacklistMappingRequest.class).addTemplate("tax-bl-mapping-request", new Rule() {{
            add("taxonomy", "any > taxonomy");
            add("owner", TaxonomyOwner.PARTNER.toString());
        }});

        Fixture.of(TaxonomyBlacklistRequest.class).addTemplate(TAXONOMY_BLACKLIST_REQUEST, new Rule() {{
            add("name", "any name");

            Set<TaxonomyBlacklistMappingRequest> blacklistMappingsRequest = new HashSet(Arrays.asList(
                    TaxonomyBlacklistMappingRequest.builder().owner(TaxonomyOwner.WALMART.getName()).taxonomy("Eletrônicos > TVs").build(),
                    TaxonomyBlacklistMappingRequest.builder().owner(TaxonomyOwner.PARTNER.getName()).taxonomy("Informática").build(),
                    TaxonomyBlacklistMappingRequest.builder().owner(TaxonomyOwner.PARTNER.getName()).taxonomy("Informática > Computadores").build(),
                    // this is invalid because it is not mapped on PartnerTaxonomy above
                    TaxonomyBlacklistMappingRequest.builder().owner(TaxonomyOwner.PARTNER.getName()).taxonomy("Eletrônicos").build(),
                    TaxonomyBlacklistMappingRequest.builder().owner(TaxonomyOwner.PARTNER.getName()).taxonomy("Eletrônicos > Vídeo").build()
            ));

            add("list", blacklistMappingsRequest);
        }});

    }
}
