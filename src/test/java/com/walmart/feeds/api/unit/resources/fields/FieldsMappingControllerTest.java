package com.walmart.feeds.api.unit.resources.fields;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.fields.model.MappedFieldEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.feed.FeedService;
import com.walmart.feeds.api.core.service.fields.FieldsMappingService;
import com.walmart.feeds.api.resources.fields.FieldsMappingController;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.walmart.feeds.api.resources.fields.FieldsMappingController.URI_FIELDSDMAPPING;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by vn0gshm on 08/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class FieldsMappingControllerTest {

    //Simula requisicao ao controller
    private MockMvc mockMvc;

    //Injeta o objeto da requisicao e faz o parse do Objeto para Jsom
    private ObjectMapper mapper;

    //Injeta o meu controller
    @InjectMocks
    private FieldsMappingController fieldsMappingController = new FieldsMappingController();

    //
    @Mock
    private FieldsMappingService fieldsMappingService;

    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.fields.test.template");
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(fieldsMappingController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void testCreateFieldsMapping() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme("valid_fields_mapping_request"))))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(fieldsMappingService, times(1)).saveFieldsdMapping(Mockito.any(FieldsMappingEntity.class));
    }

    @Test
    public void createFieldsMappingNotBlank() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme("valid_fields_mapping_notblank"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verifyZeroInteractions(fieldsMappingService);
    }

    @Test
    public void testCreatedNewFieldsMappingWithEmptyMappedFields() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class)
                    .gimme("invalid_fields_mapping_request_no_mapped_fields"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(fieldsMappingService, times(0)).saveFieldsdMapping(Mockito.any(FieldsMappingEntity.class));
    }

    @Test
    public void testCreatedNewWithConflict() throws Exception {
        Mockito.doThrow(EntityAlreadyExistsException.class)
                .when(fieldsMappingService).saveFieldsdMapping(Mockito.any(FieldsMappingEntity.class));
        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class)
                    .gimme("valid_fields_mapping_request"))))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    public String jsonRequest(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }
}
