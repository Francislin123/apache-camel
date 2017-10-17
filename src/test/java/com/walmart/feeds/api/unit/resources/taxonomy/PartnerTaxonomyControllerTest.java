package com.walmart.feeds.api.unit.resources.taxonomy;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomiesMatchedTO;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import com.walmart.feeds.api.core.service.taxonomy.model.MatcherRequest;
import com.walmart.feeds.api.core.service.taxonomy.model.TaxonomyUploadReportTO;
import com.walmart.feeds.api.core.service.taxonomy.model.UploadTaxonomyMappingTO;
import com.walmart.feeds.api.core.utils.MapperUtil;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import com.walmart.feeds.api.resources.taxonomy.PartnerTaxonomyController;
import org.apache.camel.ProducerTemplate;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by vn0y942 on 07/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class PartnerTaxonomyControllerTest {

    @InjectMocks
    private PartnerTaxonomyController partnerTaxonomyController;

    @Mock
    private PartnerTaxonomyService partnerTaxonomyService;

    private static MockMvc mockMvc;

    @Mock
    private ProducerTemplate producerTemplate;

    @Before
    public void init() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.taxonomy.test.template");
        mockMvc = MockMvcBuilders.standaloneSetup(partnerTaxonomyController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();
    }

    @Test
    public void testUploadFileWhenInvalidRequest() throws Exception {
        MockMultipartFile taxonomyMappingFile = new MockMultipartFile("file", "id ".getBytes());

        when(partnerTaxonomyService.processFile(any(UploadTaxonomyMappingTO.class))).thenReturn(mock(TaxonomyUploadReportTO.class));

        mockMvc.perform(fileUpload(PartnerTaxonomyController.V1_PARTNER_TAXONOMY, "anyExistentPartnerSlug", "anyExistentFeedSlug")
                .file(taxonomyMappingFile)
                .param("name", " Taxonomy 123")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile taxonomyMappingFile = new MockMultipartFile("file", "id ".getBytes());

        when(partnerTaxonomyService.processFile(any(UploadTaxonomyMappingTO.class))).thenReturn(mock(TaxonomyUploadReportTO.class));

        mockMvc.perform(fileUpload(PartnerTaxonomyController.V1_PARTNER_TAXONOMY, "anyExistentPartnerSlug", "anyExistentFeedSlug")
                .file(taxonomyMappingFile)
                .param("name", "Taxonomy 123")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());
    }

    @Test
    public void sendAEmptyFileAndReturnAndHandleException() throws Exception {
        MockMultipartFile taxonomyMappingFile = new MockMultipartFile("file", "id ".getBytes());

        when(partnerTaxonomyService.processFile(any(UploadTaxonomyMappingTO.class))).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(fileUpload(PartnerTaxonomyController.V1_PARTNER_TAXONOMY, "anyExistentPartnerSlug", "anyExistentFeedSlug")
                .file(taxonomyMappingFile)
                .param("name", "Taxonomy 123")
                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isNotFound());

    }

    @Test
    public void deleteTaxonomyMapping() throws Exception {
        doNothing().when(partnerTaxonomyService).removeEntityBySlug("existent", "existentSlug");
        mockMvc.perform(delete(PartnerTaxonomyController.V1_PARTNER_TAXONOMY + "/existentSlug", "partnerSlug"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTaxonomyMappingWithInvalidPartner() throws Exception {
        doThrow(EntityNotFoundException.class).when(partnerTaxonomyService).removeEntityBySlug("invalidPartner", "validSlug");
        mockMvc.perform(delete(PartnerTaxonomyController.V1_PARTNER_TAXONOMY + "/validSlug", "invalidPartner"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listTaxonomyMapping() throws Exception {
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        List<PartnerTaxonomyEntity> list = new ArrayList<>();
        list.add(partnerTaxonomyEntity);
        when(partnerTaxonomyService.fetchPartnerTaxonomies(partnerTaxonomyEntity.getPartner().getSlug(), partnerTaxonomyEntity.getSlug())).thenReturn(list);
        mockMvc.perform(get(PartnerTaxonomyController.V1_PARTNER_TAXONOMY + "/{csSlug}",
                partnerTaxonomyEntity.getPartner().getSlug(), partnerTaxonomyEntity.getSlug())).andExpect(status().isOk());
    }

    @Test
    public void createCSVFile() throws Exception {
        PartnerTaxonomyEntity partnerTaxonomyEntity = Fixture.from(PartnerTaxonomyEntity.class).gimme("cs-input-ok");
        when(partnerTaxonomyService.fetchProcessedPartnerTaxonomy(partnerTaxonomyEntity.getPartner().getSlug(), partnerTaxonomyEntity.getSlug())).thenReturn(partnerTaxonomyEntity);
        mockMvc.perform(get(PartnerTaxonomyController.V1_PARTNER_TAXONOMY + "/download/{csSlug}",
                partnerTaxonomyEntity.getPartner().getSlug(), partnerTaxonomyEntity.getSlug())).andExpect(status().isOk());
    }

    @Test
    public void fetchWalmartTaxonomy() throws Exception {
        when(partnerTaxonomyService.fetchWalmartTaxonomy("anySlug", "any string")).thenReturn("any string");
        mockMvc.perform(get(PartnerTaxonomyController.V1_PARTNER_TAXONOMY + "/getWalmartTaxonomy", "anySlug").param("taxonomySlug", "anySlug").param("taxonomy", "any string")).andExpect(status().isOk());
    }

    @Test
    public void matchedTaxonomies() throws Exception {

        List<String> walmartTaxonomies = Arrays.asList("Games > Playstation 3 > Jogos para PS3", "Games > Playstation 4 > Jogos para PS4");
        MatcherRequest request = MatcherRequest.builder().walmartTaxonomies(walmartTaxonomies).build();

        Map<String, String> matched = new HashMap<>();
        matched.put(walmartTaxonomies.get(0), "Games > Consoles > PS3");
        matched.put(walmartTaxonomies.get(1), "Games > Consoles > PS4");

        when(partnerTaxonomyService.matchedPartnerTaxonomies(eq("zoom"), eq("test"), anyList()))
                .thenReturn(TaxonomiesMatchedTO.builder()
                        .matched(matched)
                        .nonMatched(new ArrayList<>())
                        .build());

        mockMvc.perform(post(PartnerTaxonomyController.V1_PARTNER_TAXONOMY + "/test/matcher", "zoom")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(MapperUtil.getMapsAsJson(request)))
                .andExpect(jsonPath("$.nonMatched", Matchers.empty()))
                .andExpect(status().isOk());
    }

    @Test
    public void matchedTaxonomiesWithNonMatched() throws Exception {

        List<String> walmartTaxonomies = Arrays.asList("Games > Playstation 3 > Jogos para PS3", "Games > Playstation 4 > Jogos para PS4");
        MatcherRequest request = MatcherRequest.builder().walmartTaxonomies(walmartTaxonomies).build();

        Map<String, String> matched = new HashMap<>();
        matched.put(walmartTaxonomies.get(0), "Games > Consoles > PS3");

        when(partnerTaxonomyService.matchedPartnerTaxonomies(eq("zoom"), eq("test"), anyList()))
                .thenReturn(TaxonomiesMatchedTO.builder()
                        .matched(matched)
                        .nonMatched(Arrays.asList(walmartTaxonomies.get(1)))
                        .build());

        mockMvc.perform(post(PartnerTaxonomyController.V1_PARTNER_TAXONOMY + "/test/matcher", "zoom")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(MapperUtil.getMapsAsJson(request)))
                .andExpect(jsonPath("$.nonMatched", Matchers.contains("Games > Playstation 4 > Jogos para PS4")))
                .andExpect(status().isOk());
    }

}
