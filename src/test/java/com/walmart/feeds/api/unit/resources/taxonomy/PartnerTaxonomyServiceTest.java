package com.walmart.feeds.api.unit.resources.taxonomy;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyHistoryRepository;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyHistory;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyServiceImpl;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.camel.PartnerTaxonomyRouteBuilder;
import com.walmart.feeds.api.resources.camel.TaxonomyMappingBindy;
import com.walmart.feeds.api.resources.taxonomy.request.UploadTaxonomyMappingTO;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.walmart.feeds.api.resources.camel.PartnerTaxonomyRouteBuilder.VALIDATE_FILE_ROUTE;
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

    @Mock
    private ProducerTemplate producerTemplate;

    @BeforeClass
    public static void init(){
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.taxonomy.test.template");
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testWhenPartnerTaxonomyIsPending() throws IOException {

        UploadTaxonomyMappingTO to = Fixture.from(UploadTaxonomyMappingTO.class).gimme("to-mapping");

        PartnerTaxonomyEntity pendingPartnerEntity = PartnerTaxonomyEntity.builder()
                .status(ImportStatus.PENDING)
            .build();

        when(partnerService.findBySlug(to.getPartnerSlug())).thenReturn(Fixture.from(PartnerEntity.class).gimme("partner_entity"));
        when(partnerTaxonomyRepository.findBySlug(to.getSlug())).thenReturn(Optional.of(pendingPartnerEntity));

        partnerTaxonomyService.processFile(to);

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testWhenPartnerTaxonomyIsInitial() throws IOException {

        UploadTaxonomyMappingTO to = Fixture.from(UploadTaxonomyMappingTO.class).gimme("to-mapping");

        PartnerTaxonomyEntity pendingPartnerEntity = PartnerTaxonomyEntity.builder()
                .status(ImportStatus.INITIAL)
                .build();

        when(partnerService.findBySlug(to.getPartnerSlug())).thenReturn(Fixture.from(PartnerEntity.class).gimme("partner_entity"));
        when(partnerTaxonomyRepository.findBySlug(to.getSlug())).thenReturn(Optional.of(pendingPartnerEntity));

        partnerTaxonomyService.processFile(to);

    }

    @Test
    public void testWhenPartnerTaxonomyIsProcessed() throws IOException {

        UploadTaxonomyMappingTO to = Fixture.from(UploadTaxonomyMappingTO.class).gimme("to-mapping");

        PartnerTaxonomyEntity pendingPartnerEntity = PartnerTaxonomyEntity.builder()
                .status(ImportStatus.PROCESSED)
                .build();

        List<TaxonomyMappingBindy> bindyList = new ArrayList<>();
        bindyList.add(Fixture.from(TaxonomyMappingBindy.class).gimme("taxonomy-bindy"));

        PartnerTaxonomyHistory history = Fixture.from(PartnerTaxonomyHistory.class).gimme("cs-history-input-ok");

        when(partnerService.findBySlug(to.getPartnerSlug())).thenReturn(Fixture.from(PartnerEntity.class).gimme("partner_entity"));
        when(partnerTaxonomyRepository.findBySlug(to.getSlug())).thenReturn(Optional.of(pendingPartnerEntity));
        when(producerTemplate.requestBody(VALIDATE_FILE_ROUTE, to.getTaxonomyMapping().getInputStream(), List.class)).thenReturn(bindyList);

        when(partnerTaxonomyRepository.saveAndFlush(any(PartnerTaxonomyEntity.class))).thenReturn(pendingPartnerEntity);
        when(partnerTaxonomyHistoryRepository.saveAndFlush(history)).thenReturn(history);

        partnerTaxonomyService.processFile(to);

        verify(partnerTaxonomyRepository, times(1)).delete(eq(pendingPartnerEntity));
        verify(partnerTaxonomyRepository, times(1)).flush();

    }

    @Test
    public void testWhenPartnerTaxonomyIsError() throws IOException {

        UploadTaxonomyMappingTO to = Fixture.from(UploadTaxonomyMappingTO.class).gimme("to-mapping");

        PartnerTaxonomyEntity pendingPartnerEntity = PartnerTaxonomyEntity.builder()
                .status(ImportStatus.ERROR)
                .build();

        List<TaxonomyMappingBindy> bindyList = new ArrayList<>();
        bindyList.add(Fixture.from(TaxonomyMappingBindy.class).gimme("taxonomy-bindy"));

        PartnerTaxonomyHistory history = Fixture.from(PartnerTaxonomyHistory.class).gimme("cs-history-input-ok");

        when(partnerService.findBySlug(to.getPartnerSlug())).thenReturn(Fixture.from(PartnerEntity.class).gimme("partner_entity"));
        when(partnerTaxonomyRepository.findBySlug(to.getSlug())).thenReturn(Optional.of(pendingPartnerEntity));
        when(producerTemplate.requestBody(VALIDATE_FILE_ROUTE, to.getTaxonomyMapping().getInputStream(), List.class)).thenReturn(bindyList);

        when(partnerTaxonomyRepository.saveAndFlush(any(PartnerTaxonomyEntity.class))).thenReturn(pendingPartnerEntity);
        when(partnerTaxonomyHistoryRepository.saveAndFlush(history)).thenReturn(history);

        partnerTaxonomyService.processFile(to);

        verify(partnerTaxonomyRepository, times(1)).delete(eq(pendingPartnerEntity));
        verify(partnerTaxonomyRepository, times(1)).flush();

    }

    @Test
    public void testWhenPartnerTaxonomyNotExists() throws IOException {

        UploadTaxonomyMappingTO to = Fixture.from(UploadTaxonomyMappingTO.class).gimme("to-mapping");

        List<TaxonomyMappingBindy> bindyList = new ArrayList<>();
        bindyList.add(Fixture.from(TaxonomyMappingBindy.class).gimme("taxonomy-bindy"));

        PartnerTaxonomyEntity partnerTaxonomyEntity = mock(PartnerTaxonomyEntity.class);

        PartnerTaxonomyHistory history = Fixture.from(PartnerTaxonomyHistory.class).gimme("cs-history-input-ok");

        when(partnerService.findBySlug(to.getPartnerSlug())).thenReturn(Fixture.from(PartnerEntity.class).gimme("partner_entity"));
        when(partnerTaxonomyRepository.findBySlug(to.getSlug())).thenReturn(Optional.empty());
        when(producerTemplate.requestBody(VALIDATE_FILE_ROUTE, to.getTaxonomyMapping().getInputStream(), List.class)).thenReturn(bindyList);
        when(partnerTaxonomyRepository.saveAndFlush(any(PartnerTaxonomyEntity.class))).thenReturn(partnerTaxonomyEntity);

        when(partnerTaxonomyHistoryRepository.saveAndFlush(history)).thenReturn(history);

        partnerTaxonomyService.processFile(to);

        verify(partnerTaxonomyRepository, never()).delete(eq(partnerTaxonomyEntity));
        verify(partnerTaxonomyRepository, never()).flush();

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
        when(partnerService.findBySlug(partnerTaxonomyEntity.getPartner().getSlug())).thenReturn(partnerTaxonomyEntity.getPartner());
        when(partnerTaxonomyRepository.findBySlugAndPartner(partnerTaxonomyEntity.getSlug(), partnerTaxonomyEntity.getPartner()))
                .thenReturn(Optional.of(partnerTaxonomyEntity));
        doNothing().when(partnerTaxonomyRepository).delete(partnerTaxonomyEntity);

        this.partnerTaxonomyService.removeEntityBySlug(partnerTaxonomyEntity.getPartner().getSlug(), partnerTaxonomyEntity.getSlug());

        verify(partnerTaxonomyRepository, times(1)).delete(eq(partnerTaxonomyEntity));

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
        PartnerTaxonomyEntity serviceResponse = this.partnerTaxonomyService.fetchProcessedPartnerTaxonomy(partnerTaxonomyEntity.getPartner().getSlug(),partnerTaxonomyEntity.getSlug());
        when(partnerService.findBySlug(partnerTaxonomyEntity.getPartner().getSlug())).thenReturn(partnerTaxonomyEntity.getPartner());
        when(partnerTaxonomyRepository.findBySlugAndPartnerAndStatus(partnerTaxonomyEntity.getSlug(),partnerTaxonomyEntity.getPartner(),ImportStatus.PROCESSED)).thenReturn(Optional.of(partnerTaxonomyEntity));
        assertEquals(partnerTaxonomyEntity,serviceResponse);
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
