package com.walmart.feeds.api.unit.resources.fields;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.service.fields.FieldsMappingService;
import com.walmart.feeds.api.resources.fields.FieldsMappingController;
import com.walmart.feeds.api.resources.fields.request.FieldsMappingRequest;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
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
import static com.walmart.feeds.api.unit.resources.fields.test.template.FieldsMappingTemplateLoader.*;
import static org.mockito.Mockito.*;

/**
 * Created by vn0gshm on 08/08/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class FieldsMappingControllerTest {

    //Simulate controller request
    private MockMvc mockMvc;

    //Injects the object from the request and parse the object to Json
    private ObjectMapper mapper;

    //Create a instance to be used by controller
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
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme(FIELDS_MAPPING_REQUEST))))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        Mockito.verify(fieldsMappingService).save(Mockito.any(FieldsMappingEntity.class));

    }

    @Test
    public void createFieldsMappingEmptyName() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme(FIELDS_MAPPING_EMPTY_NAME))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verifyZeroInteractions(fieldsMappingService);

    }

    @Test
    public void createFieldsMappingInvalidNameOnlyWhitespaces() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme(FIELDS_MAPPING_WHITESPACES_NAME))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verifyZeroInteractions(fieldsMappingService);

    }

    @Test
    public void testCreatedNewFieldsMappingWhenMappedFieldsIsEmpty() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class)
                        .gimme(FIELDS_MAPPING_REQUEST_EMPTY_MAPPED_FIELDS))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(fieldsMappingService, times(0)).save(Mockito.any(FieldsMappingEntity.class));

    }

    @Test
    public void testCreatedNewFieldsMappingWhenMappedFieldsIsInvalid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class)
                        .gimme(FIELDS_MAPPING_REQUEST_INVALID_MAPPED_FIELDS))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(fieldsMappingService, times(0)).save(Mockito.any(FieldsMappingEntity.class));
    }

    @Test
    public void testCreatedNewFieldsMappingWhenMappedFieldsHasNullElements() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class)
                        .gimme(FIELDS_MAPPING_REQUEST_NULL_MAPPED_FIELDS))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(fieldsMappingService, times(0)).save(Mockito.any(FieldsMappingEntity.class));

    }

    @Test
    public void testCreatedNewWithConflict() throws Exception {

        Mockito.doThrow(EntityAlreadyExistsException.class)
                .when(fieldsMappingService).save(Mockito.any(FieldsMappingEntity.class));

        mockMvc.perform(MockMvcRequestBuilders.post(URI_FIELDSDMAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class)
                        .gimme(FIELDS_MAPPING_REQUEST))))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    //---------------------------------- Test Create Fields Mapping end ---------------------------------------------//

    //---------------------------------- Test delete Fields Mapping begin -------------------------------------------//

    @Test
    public void testDeleteFieldsMapping() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete(FieldsMappingController.URI_FIELDSDMAPPING + "/slug")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteFieldsMappingNonExistent() throws Exception {
        doThrow(EntityNotFoundException.class).when(fieldsMappingService).delete("buscape");

        mockMvc.perform(MockMvcRequestBuilders
                .delete(FieldsMappingController.URI_FIELDSDMAPPING + "/buscape")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteFieldsMappingUnhandledException() throws Exception {
        doThrow(Exception.class).when(fieldsMappingService).delete("buscape");

        mockMvc.perform(MockMvcRequestBuilders
                .delete(FieldsMappingController.URI_FIELDSDMAPPING + "/buscape")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    //---------------------------------- Test delete Fields Mapping begin -------------------------------------------//

    @Test
    public void testUpdateFieldsMapping() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .put(FieldsMappingController.URI_FIELDSDMAPPING + "/buscape")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme(FIELDS_MAPPING_REQUEST))))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void testUpdateFieldsMappingWhenOccursConflict() throws Exception {

        doThrow(EntityAlreadyExistsException.class).when(fieldsMappingService).update(any(FieldsMappingEntity.class));

        mockMvc.perform(MockMvcRequestBuilders
                .put(FieldsMappingController.URI_FIELDSDMAPPING + "/zoom")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme(FIELDS_MAPPING_REQUEST))))
                .andExpect(MockMvcResultMatchers.status().isConflict());

    }

    @Test
    public void testUpdateFieldsMappingWithEmptyMappedFields() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .put(FieldsMappingController.URI_FIELDSDMAPPING + "/buscape")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme(FIELDS_MAPPING_REQUEST_EMPTY_MAPPED_FIELDS))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void testUpdateFieldsMappingWhenMappedFieldsHasNullElements() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .put(FieldsMappingController.URI_FIELDSDMAPPING + "/buscape")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class)
                        .gimme(FIELDS_MAPPING_REQUEST_NULL_MAPPED_FIELDS))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testUpdateFieldsMappingWhenMappedFieldsIsInvalid() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put(URI_FIELDSDMAPPING + "/buscape")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class)
                        .gimme(FIELDS_MAPPING_REQUEST_INVALID_MAPPED_FIELDS))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(fieldsMappingService, times(0)).save(Mockito.any(FieldsMappingEntity.class));
    }

    @Test
    public void testUpdateFieldsMappingOccursUnhandledException() throws Exception {
        doThrow(RuntimeException.class).when(fieldsMappingService).update(Mockito.any(FieldsMappingEntity.class));

        mockMvc.perform(MockMvcRequestBuilders
                .put(FieldsMappingController.URI_FIELDSDMAPPING + "/buscape")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(FieldsMappingRequest.class).gimme(FIELDS_MAPPING_REQUEST))))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    public String jsonRequest(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }
}
