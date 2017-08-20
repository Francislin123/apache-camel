package com.walmart.feeds.api.unit.core.utils;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.utils.TaxonomyMappingCSVHandler;
import org.apache.commons.io.FilenameUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TaxonomyMappingCSVHandlerTest {

    @BeforeClass
    public static void init(){
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.taxonomy.test.template");
    }
    @Test
    public void createCSVFile() throws IOException {
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        File returnedFile = TaxonomyMappingCSVHandler.createCSVFile(partnerTaxonomyEntity);
        assertEquals(true, returnedFile.isFile());
        assertEquals(FilenameUtils.getExtension(returnedFile.getName()), "csv");
    }


}
