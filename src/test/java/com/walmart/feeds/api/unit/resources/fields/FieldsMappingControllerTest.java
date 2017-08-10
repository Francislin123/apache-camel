package com.walmart.feeds.api.unit.resources.fields;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.service.fields.FieldsMappingService;
import com.walmart.feeds.api.resources.fields.FieldsMappingController;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.walmart.feeds.api.resources.fields.FieldsMappingController.URI_FIELDSDMAPPING;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vn0gshm on 08/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class FieldsMappingControllerTest {

    //Simulate controller request
    private MockMvc mockMvc;

    //Injects the object from the request and parse the object to Jsom
    private ObjectMapper mapper;

    //Create a stunt for the controller
    @Mock
    private FieldsMappingService fieldsMappingService;

    //Inject my controller fieldsMappingService Obs(MOCK)
    @InjectMocks
    private FieldsMappingController fieldsMappingController = new FieldsMappingController();

    //Run only once
    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.fields.test.template");
    }

    //And always run before the test
    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(fieldsMappingController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    //---------------------------------- Test Create Fields Mapping begin ------------------------------------------//

    @Test
    public void testCreateFieldsMapping() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme("valid_fields_mapping_request"))))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        Mockito.verify(fieldsMappingService, times(1)).saveFieldsdMapping(Mockito.any
                (FieldsMappingEntity.class));
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
        verify(fieldsMappingService, times(0)).saveFieldsdMapping(Mockito.any
                (FieldsMappingEntity.class));
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
    //---------------------------------- Test Create Fields Mapping end ---------------------------------------------//

    //---------------------------------- Test Listing Fields Mapping begin ------------------------------------------//

    @Test
    public void testFindAllFieldsMapping() throws Exception {
        when(fieldsMappingService.findAllFieldsMapping()).thenReturn(Fixture.from(FieldsMappingEntity.class)
                .gimme(2, "fields_mapping_entity"));
        mockMvc.perform(MockMvcRequestBuilders.get(URI_FIELDSDMAPPING).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].slug", Matchers.is("buscape")))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(fieldsMappingService).findAllFieldsMapping();
    }

    @Test
    public void testListFieldsMappingNotResult() throws Exception {
        Mockito.doThrow(RuntimeException.class)
                .when(fieldsMappingService).findAllFieldsMapping();
        mockMvc.perform(MockMvcRequestBuilders.get(URI_FIELDSDMAPPING).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    //---------------------------------- Test Listing Fields Mapping end --------------------------------------------//
    //---------------------------------- Test Update Fields Mapping begin ------------------------------------------//

    // Delete original Fields Mapping not fold Reult NoContent ok <---
    // Delete nonexistent fields mapping
    // Delete any fields mapping String Not blank
    // Delete one fild mapping and remove before

    @Test
    public void testUpdatePartner() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URI_FIELDSDMAPPING + "/slug")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme("fields_delete_request"))))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(fieldsMappingService).deleteFieldsMapping(Mockito.any(FieldsMappingEntity.class));
    }


    public String jsonRequest(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }
}
