package com.walmart.feeds.api.unit.resources.commercialstructure;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureService;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommercialStructureServiceTest {

    @InjectMocks
    private CommercialStructureService commercialStructureService = new CommercialStructureServiceImpl();

    @Mock
    private CommercialStructureRepository commercialStructureRepository;

    @BeforeClass
    public static void init(){
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.commercialstructure.test.template");
    }
    @Test
    public void testDeleteBeforeUpdate(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        when(commercialStructureRepository.findBySlug(commercialStructureEntity.getSlug())).thenReturn(Optional.of(commercialStructureEntity));
        this.commercialStructureService.loadFile(commercialStructureEntity);
        Mockito.verify(commercialStructureRepository, Mockito.times(1)).delete(commercialStructureEntity);

    }
    @Test
    public void historyRecordTest(){
        
    }
}
