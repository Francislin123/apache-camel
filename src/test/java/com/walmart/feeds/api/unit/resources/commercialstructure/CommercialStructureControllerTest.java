package com.walmart.feeds.api.unit.resources.commercialstructure;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.resources.commercialstructure.CommercialStructureController;
import com.walmart.feeds.api.resources.commercialstructure.service.CommercialStructureService;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import org.apache.camel.ProducerTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

        mockMvc = MockMvcBuilders.standaloneSetup(commercialStructureController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile commercialStructureFile = new MockMultipartFile("file", "id ".getBytes());

        doNothing().when(producerTemplate).sendBodyAndHeaders(any(String.class), any(InputStream.class), any(Map.class));

        mockMvc.perform(multipart(CommercialStructureController.V1_COMMERCIAL_STRUCTURE, "anyExistentPartnerSlug", "anyExistentFeedSlug")
                .file(commercialStructureFile).contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isCreated());
    }
    @Test
    public void sendAEmptyFileAndReturnAndHandleException() throws Exception {
        MockMultipartFile commercialStructureFile = new MockMultipartFile("file", "id ".getBytes());

        doThrow(EntityNotFoundException.class).when(producerTemplate).sendBodyAndHeaders(any(String.class), any(InputStream.class), any(Map.class));

        mockMvc.perform(multipart(CommercialStructureController.V1_COMMERCIAL_STRUCTURE, "anyExistentPartnerSlug", "anyExistentFeedSlug")
                .file(commercialStructureFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isNotFound());

    }


}
