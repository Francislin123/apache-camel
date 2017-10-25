package com.walmart.feeds.api.unit.core.service.blacklist.taxonomy.validator;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions.TaxonomyBlacklistPartnerException;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.validation.TaxonomyBlacklistPartnerValidator;
import com.walmart.feeds.api.unit.resources.blacklist.taxonomy.TaxonomyBlacklistTemplateLoader;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TaxonomyBlacklistPartnerValidatorTest {

    @BeforeClass
    public static void init() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.taxonomy");
    }

    @Test
    public void testValidatePartnerTaxonomy() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY);
        partnerTaxonomy = getSpy(partnerTaxonomy);

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);
        blacklist = getSpy(blacklist);

        TaxonomyBlacklistPartnerValidator
                .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

        verify(blacklist).getList();
        verify(partnerTaxonomy).getTaxonomyMappings();

    }

    @Test
    public void testValidatePartnerTaxonomyWhenBlacklistNotContainsPartnerTaxonomy() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY);
        partnerTaxonomy = getSpy(partnerTaxonomy);

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST_WITHOUT_PARTNER_MAPPING);
        blacklist = getSpy(blacklist);


        TaxonomyBlacklistPartnerValidator
                .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

        verify(blacklist).getList();
        verify(partnerTaxonomy, never()).getTaxonomyMappings();

    }

    @Test
    public void testValidatePartnerTaxonomyWhenSomeTaxonomyNotExists() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY);
        partnerTaxonomy = getSpy(partnerTaxonomy);

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TB_INVALID_PARTNER_TAXONOMY);
        blacklist = getSpy(blacklist);


        try {
            TaxonomyBlacklistPartnerValidator
                    .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);
            fail("TaxonomyBlacklistPartnerException was expected!");
        } catch (TaxonomyBlacklistPartnerException e) {
            assertNotNull(e.getErrors());
            assertFalse(e.getErrors().isEmpty());
            assertTrue(e.getErrors().stream().anyMatch(error -> "Eletrônicos".equals(error.getMessage())));
        }

    }

    @Test
    public void testValidatePartnerTaxonomyWhenBlacklistContainsNullElements() {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY);
        partnerTaxonomy = getSpy(partnerTaxonomy);

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TB_INVALID_PARTNER_TAXONOMY_NULL_ELEMENT);
        blacklist = getSpy(blacklist);

        TaxonomyBlacklistPartnerValidator
                .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

        verify(blacklist).getList();
        verify(partnerTaxonomy).getTaxonomyMappings();

    }

    @Test
    public void testValidatePartnerTaxonomyWhenBlacklistIsNull() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY);
        partnerTaxonomy = getSpy(partnerTaxonomy);

        TaxonomyBlacklistEntity blacklist = null;

        TaxonomyBlacklistPartnerValidator
            .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

        verify(partnerTaxonomy, never()).getTaxonomyMappings();

    }

    @Test
    public void testValidatePartnerTaxonomyWhenPartnerTaxonomyIsNull() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = null;

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);
        blacklist = getSpy(blacklist);


        TaxonomyBlacklistPartnerValidator
            .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

        verify(blacklist, never()).getList();

    }

    public <T> T getSpy(T object) {
        return spy(object);
    }

}
