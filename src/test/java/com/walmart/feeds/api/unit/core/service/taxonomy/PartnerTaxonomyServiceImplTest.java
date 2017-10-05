package com.walmart.feeds.api.unit.core.service.taxonomy;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.camel.TaxonomyMappingBindy;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyHistoryRepository;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.taxonomy.TaxonomyMappingRepository;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyHistory;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomiesMatcherTO;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyServiceImpl;
import com.walmart.feeds.api.core.service.taxonomy.model.TaxonomyUploadReportTO;
import com.walmart.feeds.api.core.service.taxonomy.model.UploadTaxonomyMappingTO;
import org.apache.camel.ProducerTemplate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.PARSE_FILE_ROUTE;
import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.VALIDATE_FILE_ROUTE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartnerTaxonomyServiceImplTest {

    @InjectMocks
    private PartnerTaxonomyService partnerTaxonomyService = new PartnerTaxonomyServiceImpl();

    @Mock
    private PartnerTaxonomyRepository partnerTaxonomyRepository;

    @Mock
    private PartnerTaxonomyHistoryRepository partnerTaxonomyHistoryRepository;

    @Mock
    private TaxonomyMappingRepository taxonomyMappingRepository;

    @Mock
    private PartnerService partnerService;

    @Mock
    private ProducerTemplate producerTemplate;

    @BeforeClass
    public static void init(){
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.taxonomy.test.template");
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testWhenImportTaxonomyForDifferentPartners() throws IOException {

        UploadTaxonomyMappingTO to = Fixture.from(UploadTaxonomyMappingTO.class).gimme("to-mapping");

        PartnerTaxonomyEntity pendingPartnerEntity = PartnerTaxonomyEntity.builder()
                .status(ImportStatus.PROCESSED)
                .partner(mock(PartnerEntity.class))
                .build();

        when(partnerService.findBySlug(to.getPartnerSlug())).thenReturn(Fixture.from(PartnerEntity.class).gimme("partner_entity"));
        when(partnerTaxonomyRepository.findBySlug(to.getSlug())).thenReturn(Optional.of(pendingPartnerEntity));

        partnerTaxonomyService.processFile(to);

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

        PartnerEntity partnerEntity = Fixture.from(PartnerEntity.class).gimme("partner_entity");

        PartnerTaxonomyEntity pendingPartnerEntity = PartnerTaxonomyEntity.builder()
                .status(ImportStatus.PROCESSED)
                .partner(partnerEntity)
                .build();

        List<TaxonomyMappingBindy> bindyList = new ArrayList<>();
        bindyList.add(Fixture.from(TaxonomyMappingBindy.class).gimme("taxonomy-bindy"));

        PartnerTaxonomyHistory history = Fixture.from(PartnerTaxonomyHistory.class).gimme("cs-history-input-ok");

        when(partnerService.findBySlug(to.getPartnerSlug())).thenReturn(partnerEntity);
        when(partnerTaxonomyRepository.findBySlug(to.getSlug())).thenReturn(Optional.of(pendingPartnerEntity));
        when(producerTemplate.requestBody(VALIDATE_FILE_ROUTE, to.getTaxonomyMapping().getInputStream(), List.class)).thenReturn(bindyList);
        when(producerTemplate.requestBodyAndHeaders(eq(PARSE_FILE_ROUTE), any(TaxonomyMappingBindy.class), anyMapOf(String.class, Object.class), eq(TaxonomyUploadReportTO.class))).thenReturn(mock(TaxonomyUploadReportTO.class));

        when(partnerTaxonomyRepository.saveAndFlush(any(PartnerTaxonomyEntity.class))).thenReturn(pendingPartnerEntity);
        when(partnerTaxonomyHistoryRepository.saveAndFlush(history)).thenReturn(history);

        partnerTaxonomyService.processFile(to);

        verify(producerTemplate, times(1)).requestBody(eq(VALIDATE_FILE_ROUTE), any(InputStream.class), eq(List.class));
        verify(partnerTaxonomyRepository, times(1)).saveAndFlush(any(PartnerTaxonomyEntity.class));
        verify(partnerTaxonomyHistoryRepository, times(1)).saveAndFlush(any(PartnerTaxonomyHistory.class));

    }

    @Test
    public void testWhenPartnerTaxonomyIsError() throws IOException {

        UploadTaxonomyMappingTO to = Fixture.from(UploadTaxonomyMappingTO.class).gimme("to-mapping");

        PartnerEntity partnerEntity = Fixture.from(PartnerEntity.class).gimme("partner_entity");

        PartnerTaxonomyEntity pendingPartnerEntity = PartnerTaxonomyEntity.builder()
                .status(ImportStatus.ERROR)
                .partner(partnerEntity)
                .build();

        List<TaxonomyMappingBindy> bindyList = new ArrayList<>();
        bindyList.add(Fixture.from(TaxonomyMappingBindy.class).gimme("taxonomy-bindy"));

        PartnerTaxonomyHistory history = Fixture.from(PartnerTaxonomyHistory.class).gimme("cs-history-input-ok");

        when(partnerService.findBySlug(to.getPartnerSlug())).thenReturn(partnerEntity);
        when(partnerTaxonomyRepository.findBySlug(to.getSlug())).thenReturn(Optional.of(pendingPartnerEntity));
        when(producerTemplate.requestBody(VALIDATE_FILE_ROUTE, to.getTaxonomyMapping().getInputStream(), List.class)).thenReturn(bindyList);
        when(producerTemplate.requestBodyAndHeaders(eq(PARSE_FILE_ROUTE), any(TaxonomyMappingBindy.class), anyMapOf(String.class, Object.class), eq(TaxonomyUploadReportTO.class))).thenReturn(mock(TaxonomyUploadReportTO.class));

        when(partnerTaxonomyRepository.saveAndFlush(any(PartnerTaxonomyEntity.class))).thenReturn(pendingPartnerEntity);
        when(partnerTaxonomyHistoryRepository.saveAndFlush(history)).thenReturn(history);

        partnerTaxonomyService.processFile(to);

        verify(producerTemplate, times(1)).requestBody(eq(VALIDATE_FILE_ROUTE), any(InputStream.class), eq(List.class));
        verify(partnerTaxonomyRepository, times(1)).saveAndFlush(any(PartnerTaxonomyEntity.class));
        verify(partnerTaxonomyHistoryRepository, times(1)).saveAndFlush(any(PartnerTaxonomyHistory.class));


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
        when(producerTemplate.requestBodyAndHeaders(eq(PARSE_FILE_ROUTE), any(TaxonomyMappingBindy.class), anyMapOf(String.class, Object.class), eq(TaxonomyUploadReportTO.class))).thenReturn(mock(TaxonomyUploadReportTO.class));
        when(partnerTaxonomyRepository.saveAndFlush(any(PartnerTaxonomyEntity.class))).thenReturn(partnerTaxonomyEntity);

        when(partnerTaxonomyHistoryRepository.saveAndFlush(history)).thenReturn(history);

        partnerTaxonomyService.processFile(to);

        verify(partnerTaxonomyRepository, never()).delete(eq(partnerTaxonomyEntity));
        verify(partnerTaxonomyRepository, never()).flush();

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

        PartnerEntity partnerEntity = Fixture.from(PartnerEntity.class).gimme("partner_entity");

        when(partnerService.findBySlug(partnerEntity.getSlug())).thenReturn(partnerEntity);
        doThrow(EntityNotFoundException.class).when(partnerTaxonomyRepository).findBySlugAndPartner("invalidSlug", partnerEntity);

        this.partnerTaxonomyService.removeEntityBySlug(partnerEntity.getSlug(), "invalidSlug");

    }
    @Test
    public void fetchTaxonomyBySlugAndPartner(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerService.findBySlug(partnerTaxonomyEntity.getPartner().getSlug())).thenReturn(partnerTaxonomyEntity.getPartner());
        when(partnerTaxonomyRepository.findBySlugAndPartnerAndStatus(partnerTaxonomyEntity.getSlug(),partnerTaxonomyEntity.getPartner(),ImportStatus.PROCESSED)).thenReturn(Optional.of(partnerTaxonomyEntity));
        PartnerTaxonomyEntity serviceResponse = this.partnerTaxonomyService.fetchProcessedPartnerTaxonomy(partnerTaxonomyEntity.getPartner().getSlug(),partnerTaxonomyEntity.getSlug());
        assertEquals(partnerTaxonomyEntity,serviceResponse);
    }
    @Test(expected = EntityNotFoundException.class)
    public void fetchTaxonomyBySlugAndPartnerAndThrowAnException(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerService.findBySlug(partnerTaxonomyEntity.getPartner().getSlug())).thenReturn(partnerTaxonomyEntity.getPartner());
        when(partnerTaxonomyRepository.findBySlugAndPartnerAndStatus(partnerTaxonomyEntity.getSlug(),partnerTaxonomyEntity.getPartner(),ImportStatus.PROCESSED)).thenThrow(EntityNotFoundException.class);
        this.partnerTaxonomyService.fetchProcessedPartnerTaxonomy(partnerTaxonomyEntity.getPartner().getSlug(),partnerTaxonomyEntity.getSlug());
    }

    @Test
    public void saveWithHistoryWhenTernaryIsNull(){
        PartnerTaxonomyEntity entity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-mapping-null");
        when(partnerTaxonomyRepository.saveAndFlush(entity)).thenReturn(entity);

        this.partnerTaxonomyService.saveWithHistory(entity);

        ArgumentCaptor<PartnerTaxonomyHistory> argument = ArgumentCaptor.forClass(PartnerTaxonomyHistory.class);
        verify(partnerTaxonomyHistoryRepository).saveAndFlush(argument.capture());

        assertEquals(null,argument.getValue().getTaxonomyMappings());

    }

    @Test
    public void saveWithHistoryWhenTernaryNotNull(){
        PartnerTaxonomyEntity entity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerTaxonomyRepository.saveAndFlush(entity)).thenReturn(entity);

        this.partnerTaxonomyService.saveWithHistory(entity);

        ArgumentCaptor<PartnerTaxonomyHistory> argument = ArgumentCaptor.forClass(PartnerTaxonomyHistory.class);
        verify(partnerTaxonomyHistoryRepository).saveAndFlush(argument.capture());

        assertEquals(entity.getTaxonomyMappings().get(0).getPartnerPathId(),argument.getValue().getTaxonomyMappings().get(0).getPartnerPathId());

    }
    @Test(expected = EntityNotFoundException.class)
    public void fetchPartnerTaxonomiesWhenSlugIsEmptyAndThrowException(){
        PartnerEntity partnerEntity = Fixture.from(PartnerEntity.class).gimme("partner_entity");
        when(partnerService.findBySlug(partnerEntity.getSlug())).thenReturn(partnerEntity);
        when(partnerTaxonomyRepository.findByPartner(partnerEntity)).thenThrow(EntityNotFoundException.class);
        this.partnerTaxonomyService.fetchPartnerTaxonomies(partnerEntity.getSlug(),null);

    }
    @Test
    public void fetchPartnerTaxonomiesWhenSlugIsNotNull(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        PartnerEntity partnerEntity = Fixture.from(PartnerEntity.class).gimme("partner_entity");
        when(partnerService.findBySlug(partnerEntity.getSlug())).thenReturn(partnerEntity);
        when(partnerTaxonomyRepository.findByPartner(partnerEntity)).thenThrow(EntityNotFoundException.class);
        when(partnerTaxonomyRepository.findBySlugAndPartner(partnerTaxonomyEntity.getSlug(), partnerEntity)).thenReturn(Optional.of(partnerTaxonomyEntity));
        List<PartnerTaxonomyEntity> returnList = this.partnerTaxonomyService.fetchPartnerTaxonomies(partnerEntity.getSlug(),partnerTaxonomyEntity.getSlug());
        assertNotEquals(0 , returnList.size());

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

    @Test
    public void fetchWalmartTaxonomyNotEmpty(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerTaxonomyRepository.findBySlug("anyValidSlug")).thenReturn(Optional.of(partnerTaxonomyEntity));
        String response = this.partnerTaxonomyService.fetchWalmartTaxonomy("anyValidSlug", "any string");
        assertEquals("any string", response);
    }

    @Test
    public void fetchWalmartTaxonomyEmpty(){
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerTaxonomyRepository.findBySlug("anyValidSlug")).thenReturn(Optional.of(partnerTaxonomyEntity));
        String response = this.partnerTaxonomyService.fetchWalmartTaxonomy("anyValidSlug", "empty");
        assertEquals("", response);
    }


    @Test
    public void matchedTaxonomies() throws Exception {
        List<String> walmartTaxonomies = Arrays.asList("Games > Playstation 3 > Jogos para PS3", "Games > Playstation 4 > Jogos para PS4");

        when(taxonomyMappingRepository.findMappingByPartner("zoom", "test", walmartTaxonomies.get(0))).thenReturn("Games > Consoles > PS3");
        when(taxonomyMappingRepository.findMappingByPartner("zoom", "test", walmartTaxonomies.get(1))).thenReturn("Games > Consoles > PS4");

        TaxonomiesMatcherTO result = partnerTaxonomyService.matchedTaxonomies("zoom", "test", walmartTaxonomies);

        assertNotNull(result);
        assertTrue(result.getMatched().get("Games > Playstation 3 > Jogos para PS3").equals("Games > Consoles > PS3"));
        assertTrue(result.getNonMatched().isEmpty());
    }

    @Test
    public void matchedTaxonomiesWithNonMatched() throws Exception {
        List<String> walmartTaxonomies = Arrays.asList("Games > Playstation 3 > Jogos para PS3", "Games > Playstation 4 > Jogos para PS4");

        when(taxonomyMappingRepository.findMappingByPartner("zoom", "test", walmartTaxonomies.get(0))).thenReturn("Games > Consoles > PS3");

        TaxonomiesMatcherTO result = partnerTaxonomyService.matchedTaxonomies("zoom", "test", walmartTaxonomies);

        assertNotNull(result);
        assertTrue(result.getMatched().get("Games > Playstation 3 > Jogos para PS3").equals("Games > Consoles > PS3"));
        assertFalse(result.getNonMatched().isEmpty());
    }

}
