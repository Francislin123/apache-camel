package com.walmart.feeds.api.unit.core.service.blacklist.taxonomy.validator;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistMapping;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyOwner;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.validation.TaxonomyBlacklistPartnerValidator;
import com.walmart.feeds.api.unit.resources.blacklist.taxonomy.TaxonomyBlacklistTemplate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaxonomyBlacklistPartnerValidatorTest {

    private static final String BLACKLIST_SLUG = "blacklist";

    @BeforeClass
    public static void init() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.taxonomy");
    }

    @Test
    public void testValidatePartnerTaxonomy() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplate.TAXONOMY);

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplate.TAXONOMY_BLACKLIST);

        List<String> nonMatched = TaxonomyBlacklistPartnerValidator
                .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

        System.out.println(nonMatched);

        assertTrue(nonMatched.isEmpty());
    }

    @Test
    public void testValidatePartnerTaxonomyWhenBlacklistNotContainsPartnerTaxonomy() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplate.TAXONOMY);

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplate.TAXONOMY_BLACKLIST_WITHOUT_PARTNER_MAPPING);

        List<String> nonMatched = TaxonomyBlacklistPartnerValidator
                .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

        System.out.println(nonMatched);

        assertTrue(nonMatched.isEmpty());
    }

    @Test
    public void testValidatePartnerTaxonomyWhenSomeTaxonomyNotExists() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplate.TAXONOMY);

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplate.TB_INVALID_PARTNER_TAXONOMY);

        List<String> nonMatched = TaxonomyBlacklistPartnerValidator
                .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

        System.out.println(nonMatched);

        assertEquals(1, nonMatched.size());
        assertEquals("Eletrônicos", nonMatched.get(0));
    }

    @Test(expected = UserException.class)
    public void testValidatePartnerTaxonomyWhenPartnerTaxonomyIsNull() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = Fixture
                .from(PartnerTaxonomyEntity.class)
                .gimme(TaxonomyBlacklistTemplate.TAXONOMY);

        TaxonomyBlacklistEntity blacklist = null;

        List<String> nonMatched = TaxonomyBlacklistPartnerValidator
                .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

    }

    @Test(expected = UserException.class)
    public void testValidatePartnerTaxonomyWhenBlacklistIsNull() throws Exception {

        PartnerTaxonomyEntity partnerTaxonomy = null;

        TaxonomyBlacklistEntity blacklist = Fixture
                .from(TaxonomyBlacklistEntity.class)
                .gimme(TaxonomyBlacklistTemplate.TB_INVALID_PARTNER_TAXONOMY);

        List<String> nonMatched = TaxonomyBlacklistPartnerValidator
                .validatePartnerTaxonomiesOnBlacklist(blacklist, partnerTaxonomy);

    }

}
