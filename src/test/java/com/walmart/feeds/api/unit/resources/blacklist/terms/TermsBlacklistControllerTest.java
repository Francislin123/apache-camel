package com.walmart.feeds.api.unit.resources.blacklist.terms;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TermsBlacklistService;
import com.walmart.feeds.api.resources.blacklist.TermsBlacklistController;
import com.walmart.feeds.api.resources.blacklist.request.TermsBlacklistRequest;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import lombok.SneakyThrows;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.walmart.feeds.api.resources.blacklist.TermsBlacklistController.URI_TERMS_BLACKLIST;
import static com.walmart.feeds.api.unit.resources.blacklist.terms.TermsBlackListTemplateLoader.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TermsBlacklistControllerTest {

    // Simulate controller request
    private MockMvc mockMvc;

    // Injects the object from the request and parse the object to Json
    private ObjectMapper mapper;

    // Create a instance to be used by controller
    @Mock
    private TermsBlacklistService termsBlacklistService;

    // Inject my controller termsBlacklistService Obs(MOCK)
    @InjectMocks
    private TermsBlacklistController termsBlacklistController = new TermsBlacklistController();

    // Run only once
    @BeforeClass
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.terms");
    }

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(termsBlacklistController).setControllerAdvice(new FeedsAdminAPIExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    // ---------------------------------- Test Create Terms Blacklist begin -----------------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testCreateSuccessTermsBlackList() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_VALID))))
                .andExpect(status().isCreated());

        Mockito.verify(termsBlacklistService).saveTermsBlacklist(any(TermsBlacklistEntity.class));
    }

    @Test
    @SneakyThrows
    public void testCreateTermsBlackListNotFound() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class)
                        .gimme(TERMS_BLACKLIST_REQUEST_INVALID))))
                .andExpect(status().isBadRequest());


        Mockito.verify(termsBlacklistService, times(0)).saveTermsBlacklist(any(TermsBlacklistEntity.class));
    }

    @Test
    @SneakyThrows
    public void testCreatedNewTermsBlackListHasFieldsNull() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class)
                        .gimme(TERMS_BLACKLIST_REQUEST_NULL))))
                .andExpect(status().isBadRequest());

        Mockito.verify(termsBlacklistService, times(0)).saveTermsBlacklist(any(TermsBlacklistEntity.class));
    }

    @Test
    @SneakyThrows
    public void testCreatedNewTermsBlackLisWithConflict() {

        Mockito.doThrow(EntityAlreadyExistsException.class).when(termsBlacklistService)
                .saveTermsBlacklist(any(TermsBlacklistEntity.class));

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class)
                        .gimme(TERMS_BLACKLIST_REQUEST_VALID))))
                .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    public void testCreateTermsBlackListEmptyName() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_EMPTY_NAME))))
                .andExpect(status().isBadRequest());

        Mockito.verifyZeroInteractions(termsBlacklistService);
    }

    @Test
    @SneakyThrows
    public void testCreateTermsBlackListInvalidNameOnlyWhitespaces() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_EMPTY_NAME))))
                .andExpect(status().isBadRequest());

        Mockito.verifyZeroInteractions(termsBlacklistService);
    }

    // ---------------------------------- Test Create Terms Blacklist end -------------------------------------------------------------------//


    // ---------------------------------- Test Update Terms Blacklist begin -----------------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testUpdateSuccessTermsBlackList() {

        mockMvc.perform(MockMvcRequestBuilders
                .put(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_VALID))))
                .andExpect(status().isOk());

        Mockito.verify(termsBlacklistService, times(1)).updateTermsBlacklist(any(TermsBlacklistEntity.class));
    }

    @Test
    public void testUpdateTermsBlackListWhenOccursConflict() throws Exception {

        Mockito.doThrow(EntityAlreadyExistsException.class).when(termsBlacklistService).updateTermsBlacklist(any(TermsBlacklistEntity.class));

        mockMvc.perform(MockMvcRequestBuilders
                .put(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_VALID))))
                .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    public void testUpdateTermsBlackListWithEmptyFields() {

        mockMvc.perform(MockMvcRequestBuilders
                .put(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_EMPTY_LIST))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void testUpdateTermsBlackListWhenListFieldsHasNullElements() {

        mockMvc.perform(MockMvcRequestBuilders
                .put(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class)
                        .gimme(TERMS_BLACKLIST_REQUEST_NULL))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    public void testUpdateFieldsMappingWhenMappedFieldsIsInvalid() {

        mockMvc.perform(MockMvcRequestBuilders.put(URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class)
                        .gimme(TERMS_BLACKLIST_REQUEST_INVALID))))
                .andExpect(status().isBadRequest());

        Mockito.verify(termsBlacklistService, times(0)).saveTermsBlacklist(Mockito.any(TermsBlacklistEntity.class));
    }

    @Test
    @SneakyThrows
    public void testUpdateTermsBlackListOccursUnhandledException() {

        Mockito.doThrow(RuntimeException.class).when(termsBlacklistService).updateTermsBlacklist(Mockito.any(TermsBlacklistEntity.class));

        mockMvc.perform(MockMvcRequestBuilders
                .put(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_VALID))))
                .andExpect(status().isInternalServerError());
    }

    // ---------------------------------- Test Update Terms Blacklist end -------------------------------------------------------------------//

    // ---------------------------------- Test Delete Terms Blacklist begin -----------------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testDeleteTermsBlackList() {

        mockMvc.perform(MockMvcRequestBuilders
                .delete(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void testDeleteTermsBlackListNonExistent() {

        Mockito.doThrow(EntityNotFoundException.class).when(termsBlacklistService).deleteTermsBlacklist("facebook-terms-blacklist");

        mockMvc.perform(MockMvcRequestBuilders
                .delete(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testDeleteTermsBlackListUnhandledException() {

        Mockito.doThrow(Exception.class).when(termsBlacklistService).deleteTermsBlacklist("facebook-terms-blacklist");

        mockMvc.perform(MockMvcRequestBuilders
                .delete(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    // ---------------------------------- Test Delete Terms Blacklist end -------------------------------------------------------------------//

    // ---------------------------------- Test List Terms Blacklist begin -------------------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testListTermsBlacklistSuccess() {

        mockMvc.perform(
                get(TermsBlacklistController.URI_TERMS_BLACKLIST)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Mockito.verify(termsBlacklistService, times(1)).findAllTermsBlacklistEntity();

    }

    @Test
    @SneakyThrows
    public void testListTermsBlacklistNonExistent() {

        Mockito.doThrow(EntityNotFoundException.class).when(termsBlacklistService).findAllTermsBlacklistEntity();

        mockMvc.perform(
                get(TermsBlacklistController.URI_TERMS_BLACKLIST)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testListTermsBlackListUnhandledException() {

        Mockito.doThrow(Exception.class).when(termsBlacklistService).findAllTermsBlacklistEntity();

        mockMvc.perform(
                get(TermsBlacklistController.URI_TERMS_BLACKLIST)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    // ---------------------------------- Test List Terms Blacklist end   -------------------------------------------------------------------//


    // ---------------------------------- Test List Fetch all Terms Blacklist begin ---------------------------------------------------------//


    // ---------------------------------- Test List Fetch all Terms Blacklist end -----------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testTermsBlackListFetchAll() {

        when(termsBlacklistService.findAllTermsBlacklistEntity())
                .thenReturn(Fixture.from(TermsBlacklistEntity.class).gimme(2, TermsBlackListTemplateLoader.TERMS_BLACKLIST_REQUEST_VALID));

        mockMvc.perform(get(TermsBlacklistController.URI_TERMS_BLACKLIST).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result").isNotEmpty());

    }

    @Test
    @SneakyThrows
    public void testTermsBlackListFetchAllWhenOccursRuntimeException() {

        when(termsBlacklistService.findAllTermsBlacklistEntity()).thenThrow(RuntimeException.class);

        mockMvc.perform(get(TermsBlacklistController.URI_TERMS_BLACKLIST).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    public void testTermsBlackListFetchAllInternalServerError() {

        Mockito.doThrow(Exception.class).when(termsBlacklistService).findAllTermsBlacklistEntity();

        mockMvc.perform(get(TermsBlacklistController.URI_TERMS_BLACKLIST).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    // ---------------------------------- Test List findBySlug Terms Blacklist begin --------------------------------------------------------//

    @Test
    @SneakyThrows
    public void testFetchTermsBlackListBySlug() {

        String slug = "facebook-terms-blacklist";
        when(termsBlacklistService.findBySlug(anyString())).thenReturn(Fixture.from(TermsBlacklistEntity.class)
                .gimme(TermsBlackListTemplateLoader.TERMS_BLACKLIST_ENTITY_VALID_BY_SLUG));

        mockMvc.perform(get(TermsBlacklistController.URI_TERMS_BLACKLIST + "/" + slug)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").value("Facebook Terms Blacklist"));

    }

    @Test
    @SneakyThrows
    public void testFetchTermsBlackListBySlugNotFound() {

        String slug = "facebook-terms-blacklist";
        when(termsBlacklistService.findBySlug(slug)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get(TermsBlacklistController.URI_TERMS_BLACKLIST + "/" + slug)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testFetchTermsBlackListBySlugInternalServerError() {

        Mockito.doThrow(Exception.class).when(termsBlacklistService).deleteTermsBlacklist("facebook-terms-blacklist");

        mockMvc.perform(MockMvcRequestBuilders
                .get(TermsBlacklistController.URI_TERMS_BLACKLIST + "/facebook-terms-blacklist")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());
    }

    // ---------------------------------- Test List findBySlug Terms Blacklist end ----------------------------------------------------------//

    public String jsonRequest(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }


}
