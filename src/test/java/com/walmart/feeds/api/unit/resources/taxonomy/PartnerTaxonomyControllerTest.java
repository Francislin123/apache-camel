package com.walmart.feeds.api.unit.resources.taxonomy;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.service.taxonomy.PartnerTaxonomyService;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import com.walmart.feeds.api.resources.taxonomy.PartnerTaxonomyController;
import com.walmart.feeds.api.resources.taxonomy.request.UploadTaxonomyMappingTO;
import org.apache.camel.ProducerTemplate;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void testUploadFile() throws Exception {
        MockMultipartFile taxonomyMappingFile = new MockMultipartFile("file", "id ".getBytes());

        doNothing().when(partnerTaxonomyService).processFile(mock(UploadTaxonomyMappingTO.class));

        mockMvc.perform(fileUpload(PartnerTaxonomyController.V1_PARTNER_TAXONOMY, "anyExistentPartnerSlug", "anyExistentFeedSlug")
                .file(taxonomyMappingFile)
                .param("name", "Taxonomy 123")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isAccepted());
    }
    @Test
    public void sendAEmptyFileAndReturnAndHandleException() throws Exception {
        MockMultipartFile taxonomyMappingFile = new MockMultipartFile("file", "id ".getBytes());

        doThrow(EntityNotFoundException.class).when(partnerTaxonomyService).processFile(any(UploadTaxonomyMappingTO.class));

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
        when(partnerTaxonomyService.fetchPartnerTaxonomy(partnerTaxonomyEntity.getPartner().getSlug(), partnerTaxonomyEntity.getSlug())).thenReturn(partnerTaxonomyEntity);
        mockMvc.perform(get(PartnerTaxonomyController.V1_PARTNER_TAXONOMY + "/download/{csSlug}",
                partnerTaxonomyEntity.getPartner().getSlug(), partnerTaxonomyEntity.getSlug())).andExpect(status().isOk());
    }



}
