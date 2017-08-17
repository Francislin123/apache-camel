package com.walmart.feeds.api.unit.resources.commercialstructure;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.service.commercialstructure.CommercialStructureService;
import com.walmart.feeds.api.resources.commercialstructure.CommercialStructureController;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by vn0y942 on 07/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommercialStructureControllerTest{

    @InjectMocks
    private CommercialStructureController commercialStructureController;

    @Mock
    private CommercialStructureService commercialStructureService;

    private static MockMvc mockMvc;


    @Mock
    private ProducerTemplate producerTemplate;

    @Before
    public void init() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.commercialstructure.test.template");
        mockMvc = MockMvcBuilders.standaloneSetup(commercialStructureController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile commercialStructureFile = new MockMultipartFile("file", "id ".getBytes());

        doNothing().when(producerTemplate).sendBodyAndHeaders(any(String.class), any(InputStream.class), any(Map.class));

        mockMvc.perform(fileUpload(CommercialStructureController.V1_COMMERCIAL_STRUCTURE, "anyExistentPartnerSlug", "anyExistentFeedSlug")
                .file(commercialStructureFile).contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isCreated());
    }
    @Test
    public void sendAEmptyFileAndReturnAndHandleException() throws Exception {
        MockMultipartFile commercialStructureFile = new MockMultipartFile("file", "id ".getBytes());

        doThrow(EntityNotFoundException.class).when(producerTemplate).sendBodyAndHeaders(any(String.class), any(InputStream.class), any(Map.class));

        mockMvc.perform(fileUpload(CommercialStructureController.V1_COMMERCIAL_STRUCTURE, "anyExistentPartnerSlug", "anyExistentFeedSlug")
                .file(commercialStructureFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isNotFound());

    }
    @Test
    public void deleteCommercialStructureMapping() throws Exception {
        doNothing().when(commercialStructureService).removeEntityBySlug("existent", "existentSlug");
        mockMvc.perform(delete(CommercialStructureController.V1_COMMERCIAL_STRUCTURE + "/existentSlug", "partnerSlug"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCommercialStructureMappingWithInvalidPartner() throws Exception {
        doThrow(EntityNotFoundException.class).when(commercialStructureService).removeEntityBySlug("invalidPartner", "validSlug");
        mockMvc.perform(delete(CommercialStructureController.V1_COMMERCIAL_STRUCTURE + "/validSlug", "invalidPartner"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listCommercialStructure() throws Exception {
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        List<CommercialStructureEntity> list = new ArrayList<>();
        list.add(commercialStructureEntity);
        when(commercialStructureService.fetchCommercialStructure(commercialStructureEntity.getPartner().getSlug(), commercialStructureEntity.getSlug())).thenReturn(list);
        mockMvc.perform(get(CommercialStructureController.V1_COMMERCIAL_STRUCTURE+ "/{csSlug}",
                commercialStructureEntity.getPartner().getSlug(), commercialStructureEntity.getSlug())).andExpect(status().isOk());
    }

    @Test
    public void createCSVFile() throws Exception {
        CommercialStructureEntity commercialStructureEntity = Fixture.from(CommercialStructureEntity.class).gimme("cs-input-ok");
        when(commercialStructureService.fetchOneCommercialStructure(commercialStructureEntity.getPartner().getSlug(), commercialStructureEntity.getSlug())).thenReturn(commercialStructureEntity);
        mockMvc.perform(get(CommercialStructureController.V1_COMMERCIAL_STRUCTURE+ "/download/{csSlug}",
                commercialStructureEntity.getPartner().getSlug(), commercialStructureEntity.getSlug())).andExpect(status().isOk());
    }



}
