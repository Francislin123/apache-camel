package com.walmart.feeds.api.unit.resources.commercialstructure;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureHistoryRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureService;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureServiceImpl;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommercialStructureServiceTest {

    @InjectMocks
    private CommercialStructureService commercialStructureService = new CommercialStructureServiceImpl();

    @Mock
    private CommercialStructureRepository commercialStructureRepository;

    @Mock
    private CommercialStructureHistoryRepository commercialStructureHistoryRepository;

    @Mock
    private PartnerService partnerService;

    @BeforeClass
    public static void init(){
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.commercialstructure.test.template");
    }
    @Test
    public void testDeleteBeforeUpdate(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        when(commercialStructureRepository.findBySlug(commercialStructureEntity.getSlug())).thenReturn(Optional.of(commercialStructureEntity));
        when(partnerService.findBySlug(commercialStructureEntity.getPartner().getSlug())).thenReturn(new PartnerEntity());
        when(commercialStructureRepository.saveAndFlush(commercialStructureEntity)).thenReturn(commercialStructureEntity);
        this.commercialStructureService.loadFile(commercialStructureEntity);
        verify(commercialStructureRepository, times(1)).delete(commercialStructureEntity);

    }
    @Test
    public void testEntityToHistoryTransform(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        CommercialStructureHistory history = this.commercialStructureService.entityToHistoryTransform(commercialStructureEntity);
        assertEquals(history.getSlug(), commercialStructureEntity.getSlug());
    }
    @Test
    public void historyRecordTest(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        when(commercialStructureRepository.findBySlug(commercialStructureEntity.getSlug())).thenReturn(Optional.of(commercialStructureEntity));
        when(commercialStructureRepository.saveAndFlush(commercialStructureEntity)).thenReturn(commercialStructureEntity);
        when(partnerService.findBySlug(commercialStructureEntity.getPartner().getSlug())).thenReturn(new PartnerEntity());
        this.commercialStructureService.loadFile(commercialStructureEntity);
        verify(commercialStructureRepository, times(1)).saveAndFlush(commercialStructureEntity);
        verify(commercialStructureHistoryRepository, times(1)).saveAndFlush(any(CommercialStructureHistory.class));
    }
    @Test
    public void deleteMappedCommercialStructure(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        when(commercialStructureRepository.findBySlug("existentSlug")).thenReturn(Optional.of(commercialStructureEntity));
        when(partnerService.findBySlug("validPartner")).thenReturn(new PartnerEntity());
        doNothing().when(commercialStructureRepository).delete(commercialStructureEntity);
        this.commercialStructureService.removeEntityBySlug("validPartner", "existentSlug");
        verify(commercialStructureRepository, times(1)).delete(commercialStructureEntity);

    }
    @Test(expected = EntityNotFoundException.class)
    public void deleteCommercialStructureWithInvalidPartner(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        doThrow(EntityNotFoundException.class).when(partnerService).findBySlug("invalidPartner");
        when(commercialStructureRepository.findBySlug("existentSlug")).thenReturn(Optional.of(commercialStructureEntity));
        this.commercialStructureService.removeEntityBySlug("invalidPartner", "existentSlug");
        verify(commercialStructureRepository, times(1)).delete(commercialStructureEntity);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteCommercialStructureWithInvalidSlug(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        when(commercialStructureRepository.findBySlug(commercialStructureEntity.getSlug())).thenReturn(Optional.of(commercialStructureEntity));
        doThrow(EntityNotFoundException.class).when(commercialStructureRepository).findBySlug("invalidSlug");
        this.commercialStructureService.removeEntityBySlug(commercialStructureEntity.getSlug(), "invalidSlug");
        verify(commercialStructureRepository, times(1)).delete(commercialStructureEntity);
    }
    @Test
    public void fetchCommercialStructureBySlugAndPartner(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        List<CommercialStructureEntity> list = new ArrayList<>();
        list.add(commercialStructureEntity);

        when(commercialStructureRepository.findBySlug(commercialStructureEntity.getSlug())).thenReturn(Optional.of(commercialStructureEntity));
        when(partnerService.findBySlug(commercialStructureEntity.getPartner().getSlug())).thenReturn(commercialStructureEntity.getPartner());
        this.commercialStructureService.fetchCommercialStructure(commercialStructureEntity.getPartner().getSlug(),commercialStructureEntity.getSlug());
        verify(commercialStructureRepository, times(1)).findBySlug(commercialStructureEntity.getSlug());

    }
    @Test
    public void fetchCommercialStructureByPartner(){
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        List<CommercialStructureEntity> list = new ArrayList<>();
        list.add(commercialStructureEntity);

        when(commercialStructureRepository.findByPartner(commercialStructureEntity.getPartner())).thenReturn(Optional.of(list));
        when(partnerService.findBySlug(commercialStructureEntity.getPartner().getSlug())).thenReturn(commercialStructureEntity.getPartner());
        this.commercialStructureService.fetchCommercialStructure(commercialStructureEntity.getPartner().getSlug(),null);
        verify(commercialStructureRepository, times(1)).findByPartner(commercialStructureEntity.getPartner());
    }



}
