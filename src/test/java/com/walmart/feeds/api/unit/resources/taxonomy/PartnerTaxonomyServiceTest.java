package com.walmart.feeds.api.unit.resources.taxonomy;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyHistoryRepository;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyHistory;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyServiceImpl;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartnerTaxonomyServiceTest {

    @InjectMocks
    private PartnerTaxonomyService partnerTaxonomyService = new PartnerTaxonomyServiceImpl();

    @Mock
    private PartnerTaxonomyRepository partnerTaxonomyRepository;

    @Mock
    private PartnerTaxonomyHistoryRepository partnerTaxonomyHistoryRepository;

    @Mock
    private PartnerService partnerService;

    @BeforeClass
    public static void init(){
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.taxonomy.test.template");
    }
    @Test
    public void testDeleteBeforeUpdate(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerTaxonomyRepository.findBySlug(partnerTaxonomyEntity.getSlug())).thenReturn(Optional.of(partnerTaxonomyEntity));
        when(partnerService.findBySlug(partnerTaxonomyEntity.getPartner().getSlug())).thenReturn(new PartnerEntity());
        when(partnerTaxonomyRepository.saveAndFlush(partnerTaxonomyEntity)).thenReturn(partnerTaxonomyEntity);
//        this.partnerTaxonomyService.loadFile(partnerTaxonomyEntity);
        verify(partnerTaxonomyRepository, times(1)).delete(partnerTaxonomyEntity);

    }

    @Test
    public void historyRecordTest(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerTaxonomyRepository.findBySlug(partnerTaxonomyEntity.getSlug())).thenReturn(Optional.of(partnerTaxonomyEntity));
        when(partnerTaxonomyRepository.saveAndFlush(partnerTaxonomyEntity)).thenReturn(partnerTaxonomyEntity);
        when(partnerService.findBySlug(partnerTaxonomyEntity.getPartner().getSlug())).thenReturn(new PartnerEntity());
//        this.partnerTaxonomyService.loadFile(partnerTaxonomyEntity);
        verify(partnerTaxonomyRepository, times(1)).saveAndFlush(partnerTaxonomyEntity);
        verify(partnerTaxonomyHistoryRepository, times(1)).saveAndFlush(any(PartnerTaxonomyHistory.class));
    }
    @Test
    public void deleteMappedTaxonomy(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerTaxonomyRepository.findBySlug("existentSlug")).thenReturn(Optional.of(partnerTaxonomyEntity));
        when(partnerService.findBySlug("validPartner")).thenReturn(new PartnerEntity());
        doNothing().when(partnerTaxonomyRepository).delete(partnerTaxonomyEntity);
        this.partnerTaxonomyService.removeEntityBySlug("validPartner", "existentSlug");
        verify(partnerTaxonomyRepository, times(1)).delete(partnerTaxonomyEntity);

    }
    @Test(expected = EntityNotFoundException.class)
    public void deleteTaxonomyWithInvalidPartner(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        doThrow(EntityNotFoundException.class).when(partnerService).findBySlug("invalidPartner");
        when(partnerTaxonomyRepository.findBySlug("existentSlug")).thenReturn(Optional.of(partnerTaxonomyEntity));
        this.partnerTaxonomyService.removeEntityBySlug("invalidPartner", "existentSlug");
        verify(partnerTaxonomyRepository, times(1)).delete(partnerTaxonomyEntity);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteTaxonomyWithInvalidSlug(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerTaxonomyRepository.findBySlug(partnerTaxonomyEntity.getSlug())).thenReturn(Optional.of(partnerTaxonomyEntity));
        doThrow(EntityNotFoundException.class).when(partnerTaxonomyRepository).findBySlug("invalidSlug");
        this.partnerTaxonomyService.removeEntityBySlug(partnerTaxonomyEntity.getSlug(), "invalidSlug");
        verify(partnerTaxonomyRepository, times(1)).delete(partnerTaxonomyEntity);
    }
    @Test
    public void fetchTaxonomyBySlugAndPartner(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        List<PartnerTaxonomyEntity> list = new ArrayList<>();
        list.add(partnerTaxonomyEntity);

        when(partnerTaxonomyRepository.findBySlug(partnerTaxonomyEntity.getSlug())).thenReturn(Optional.of(partnerTaxonomyEntity));
        when(partnerService.findBySlug(partnerTaxonomyEntity.getPartner().getSlug())).thenReturn(partnerTaxonomyEntity.getPartner());
        this.partnerTaxonomyService.fetchPartnerTaxonomies(partnerTaxonomyEntity.getPartner().getSlug(), partnerTaxonomyEntity.getSlug());
        verify(partnerTaxonomyRepository, times(1)).findBySlug(partnerTaxonomyEntity.getSlug());

    }
    @Test
    public void fetchTaxonomyByPartner(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        List<PartnerTaxonomyEntity> list = new ArrayList<>();
        list.add(partnerTaxonomyEntity);

        when(partnerTaxonomyRepository.findByPartner(partnerTaxonomyEntity.getPartner())).thenReturn(Optional.of(list));
        when(partnerService.findBySlug(partnerTaxonomyEntity.getPartner().getSlug())).thenReturn(partnerTaxonomyEntity.getPartner());
        this.partnerTaxonomyService.fetchPartnerTaxonomies(partnerTaxonomyEntity.getPartner().getSlug(),null);
        verify(partnerTaxonomyRepository, times(1)).findByPartner(partnerTaxonomyEntity.getPartner());
    }



}
