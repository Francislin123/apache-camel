package com.walmart.feeds.api.unit.core.service.feed;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationFormat;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.core.repository.feed.model.FeedType;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.unit.resources.blacklist.taxonomy.TaxonomyBlacklistTemplateLoader;
import com.walmart.feeds.api.unit.resources.blacklist.terms.TermsBlackListTemplateLoader;
import com.walmart.feeds.api.unit.resources.partner.test.template.PartnerTemplateLoader;

import java.util.*;

public class FeedEntityTemplateLoader implements TemplateLoader {

    public static final String FEED_ENTITY = "feed-entity";
    public static final String FEED_ENTITY_UPDATE_NAME = "feed-entity-updateTermsBlacklist-name";
    public static final String FEED_ENTITY_WITHOUT_TAXONOMY_BLACKLIST = "feed-entity-without-taxonomy-blacklist";
    public static final String FEED_ENTITY_WITH_INVALID_PARTNER_TAXONOMY_BLACKLIST = "feed-entity-with-invalid-partner-taxonomy-blacklist";
    public static final String TEMPLATE_ENTITY = "template-entity";
    public static final String FEED_ENTITY_NO_EXISTENT_TEMPLATE = "feed-template-no-existent";
    public static final String FEED_ENTITY_COLLECTION_ID_NULL = "feed-collection-id-null";

    @Override
    public void load() {

        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.partner.test.template");
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.taxonomy");
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.terms");

        Fixture.of(TemplateEntity.class).addTemplate(TEMPLATE_ENTITY, new Rule() {{
            add("name", "Default Template");
            add("slug", "default-template");
            add("separator", ">");
        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY, new Rule() {{

            TemplateEntity template = TemplateEntity.builder().slug("template").name("default").separator(">").body("default").format("xml").build();
            PartnerEntity partner = Fixture.from(PartnerEntity.class).gimme(PartnerTemplateLoader.PARTNER_ENTITY);
            TaxonomyBlacklistEntity blacklistEntity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);
            PartnerTaxonomyEntity partnerTaxonomy = Fixture.from(PartnerTaxonomyEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY);
            List<TermsBlacklistEntity> termsBlacklists = Fixture.from(TermsBlacklistEntity.class).gimme(1, TermsBlackListTemplateLoader.TERMS_BLACKLIST_REQUEST_VALID);

            Map<String, String> utms = new HashMap<String, String>();
            utms.put("utm_source", "Google");

            add("name", "Feed test");
            add("slug", "feed-test");
            add("active", true);
            add("utms", utms);
            add("notificationFormat", FeedNotificationFormat.JSON);
            add("notificationMethod", FeedNotificationMethod.FILE);
            add("type", FeedType.INVENTORY);
            add("collectionId", 7380L);
            add("taxonomyBlacklist", blacklistEntity);
            add("termsBlacklist", termsBlacklists);
            add("template", template);
            add("partner", partner);
            add("partnerTaxonomy", partnerTaxonomy);

        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY_NO_EXISTENT_TEMPLATE, new Rule() {{

            TemplateEntity template = TemplateEntity.builder().slug("template").name("default").separator(">").body("default").format("xml").build();
            PartnerEntity partner = Fixture.from(PartnerEntity.class).gimme(PartnerTemplateLoader.PARTNER_ENTITY);

            add("name", "Feed test");
            add("partner", partner);
            add("collectionId", 7380L);
            add("template", template);

        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY_COLLECTION_ID_NULL, new Rule() {{

            TemplateEntity template = TemplateEntity.builder().slug("template").name("default").separator(">").body("default").format("xml").build();
            PartnerEntity partner = Fixture.from(PartnerEntity.class).gimme(PartnerTemplateLoader.PARTNER_ENTITY);
            TaxonomyBlacklistEntity blacklistEntity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);
            PartnerTaxonomyEntity partnerTaxonomy = Fixture.from(PartnerTaxonomyEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY);
            List<TermsBlacklistEntity> termsBlacklists = Fixture.from(TermsBlacklistEntity.class).gimme(1, TermsBlackListTemplateLoader.TERMS_BLACKLIST_REQUEST_VALID);

            Map<String, String> utms = new HashMap<String, String>();
            utms.put("utm_source", "Google");

            add("name", "Feed test");
            add("slug", "feed-test");
            add("active", true);
            add("utms", utms);
            add("notificationFormat", FeedNotificationFormat.JSON);
            add("notificationMethod", FeedNotificationMethod.FILE);
            add("type", FeedType.INVENTORY);
            add("collectionId", null);
            add("taxonomyBlacklist", blacklistEntity);
            add("termsBlacklist", termsBlacklists);
            add("template", template);
            add("partner", partner);
            add("partnerTaxonomy", partnerTaxonomy);

        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY_UPDATE_NAME).inherits(FEED_ENTITY, new Rule() {{
            add("name", "Big teste");

        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY_WITHOUT_TAXONOMY_BLACKLIST).inherits(FEED_ENTITY, new Rule() {{
            add("taxonomyBlacklist", null);
        }});

        Fixture.of(FeedEntity.class).addTemplate(FEED_ENTITY_WITH_INVALID_PARTNER_TAXONOMY_BLACKLIST).inherits(FEED_ENTITY, new Rule() {{
            TaxonomyBlacklistEntity blacklist = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TB_INVALID_PARTNER_TAXONOMY);
            add("taxonomyBlacklist", blacklist);
        }});
    }
}
