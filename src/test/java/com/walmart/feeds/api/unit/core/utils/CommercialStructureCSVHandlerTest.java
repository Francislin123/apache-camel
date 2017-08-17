package com.walmart.feeds.api.unit.core.utils;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.utils.CommercialStructureCSVHandler;
import org.apache.commons.io.FilenameUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CommercialStructureCSVHandlerTest {

    @BeforeClass
    public static void init(){
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.commercialstructure.test.template");
    }
    @Test
    public void createCSVFile() throws IOException {
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        File returnedFile = CommercialStructureCSVHandler.createCSVFile(commercialStructureEntity);
        assertEquals(true, returnedFile.isFile());
        assertEquals(FilenameUtils.getExtension(returnedFile.getName()), "csv");
    }


}
