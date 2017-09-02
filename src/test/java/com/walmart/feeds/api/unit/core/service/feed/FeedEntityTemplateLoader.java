package com.walmart.feeds.api.unit.core.service.feed;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.function.Function;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationFormat;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingEntity;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.validation.TaxonomyBlacklistPartnerValidator;
import com.walmart.feeds.api.unit.resources.blacklist.taxonomy.TaxonomyBlacklistTemplateLoader;
import com.walmart.feeds.api.unit.resources.partner.test.template.PartnerTemplateLoader;

import java.util.Arrays;

import static com.walmart.feeds.api.core.repository.feed.model.FeedType.INVENTORY;

public class FeedEntityTemplateLoader implements TemplateLoader {

    public static final String FEED_ENTITY = "feed-entity";
    public static final String FEED_ENTITY_UPDATE_NAME = "feed-entity-update-name";
    public static final String FEED_ENTITY_WITHOUT_TAXONOMY_BLACKLIST = "feed-entity-without-taxonomy-blacklist";
    public static final String FEED_ENTITY_WITH_INVALID_PARTNER_TAXONOMY_BLACKLIST = "feed-entity-with-invalid-partner-taxonomy-blacklist";
    public static final String TEMPLATE_ENTITY = "template-entity";
    public static final String PARTNER_TAXONOMY_ENTITY = "partner-taxonomy-entity";

    @Override
    public void load() {

        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.partner.test.template");
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.taxonomy");

        Fixture.of(TemplateEntity.class).addTemplate(TEMPLATE_ENTITY, new Rule(){{
            add("slug", "template");
        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY, new Rule(){{

            TemplateEntity template = TemplateEntity.builder().slug("template").build();
            PartnerEntity partner = Fixture.from(PartnerEntity.class).gimme(PartnerTemplateLoader.PARTNER_ENTITY);
            TaxonomyBlacklistEntity blacklistEntity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);
            PartnerTaxonomyEntity partnerTaxonomy = Fixture.from(PartnerTaxonomyEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY);
            FieldsMappingEntity fieldsMapping = FieldsMappingEntity.builder().slug("fields-mapping").build();

            add("name", "Feed teste");
            add("slug", "feed-teste");
            add("active", true);
            add("notificationFormat", FeedNotificationFormat.JSON);
            add("notificationMethod", FeedNotificationMethod.FILE);
            add("type", FeedType.INVENTORY);
            add("collectionId", 7380L);
            add("taxonomyBlacklist", blacklistEntity);
            add("template", template);
            add("partner", partner);
            add("partnerTaxonomy", partnerTaxonomy);
            add("fieldsMapping", fieldsMapping);

        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY_UPDATE_NAME).inherits(FEED_ENTITY, new Rule(){{
            add("name", "Big teste");

        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY_WITHOUT_TAXONOMY_BLACKLIST).inherits(FEED_ENTITY, new Rule(){{
            add("taxonomyBlacklist", null);
        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY_WITH_INVALID_PARTNER_TAXONOMY_BLACKLIST).inherits(FEED_ENTITY, new Rule(){{
            TaxonomyBlacklistEntity blacklist = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TB_INVALID_PARTNER_TAXONOMY);
            add("taxonomyBlacklist", blacklist);
        }});

    }


}
