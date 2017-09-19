package com.walmart.feeds.api.unit.resources.blacklist.terms;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.walmart.feeds.api.resources.blacklist.TermsBlacklistController.URI_TERMS_BLACKLIST;
import static com.walmart.feeds.api.unit.resources.blacklist.terms.TermsBlackListTemplateLoader.*;
import static org.mockito.Mockito.times;

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
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(termsBlacklistService).saveTermsBlacklist(Mockito.any(TermsBlacklistEntity.class));

    }

    @Test
    @SneakyThrows
    public void testCreateTermsBlackListNotFound() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class)
                        .gimme(TERMS_BLACKLIST_REQUEST_INVALID))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


        Mockito.verify(termsBlacklistService, times(0)).saveTermsBlacklist(Mockito.any(TermsBlacklistEntity.class));
    }

    @Test
    @SneakyThrows
    public void testCreatedNewTermsBlackListHasFieldsNull() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class)
                        .gimme(TERMS_BLACKLIST_REQUEST_NULL))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(termsBlacklistService, times(0)).saveTermsBlacklist(Mockito.any(TermsBlacklistEntity.class));
    }

    @Test
    @SneakyThrows
    public void testCreatedNewTermsBlackLisWithConflict() {

        Mockito.doThrow(EntityAlreadyExistsException.class).when(termsBlacklistService)
                .saveTermsBlacklist(Mockito.any(TermsBlacklistEntity.class));

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class)
                        .gimme(TERMS_BLACKLIST_REQUEST_VALID))))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @SneakyThrows
    public void testCreateTermsBlackListEmptyName() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_EMPTY_NAME))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verifyZeroInteractions(termsBlacklistService);

    }

    @Test
    @SneakyThrows
    public void testCreateTermsBlackListInvalidNameOnlyWhitespaces() {

        mockMvc.perform(MockMvcRequestBuilders.post(URI_TERMS_BLACKLIST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRequest(Fixture.from(TermsBlacklistRequest.class).gimme(TERMS_BLACKLIST_REQUEST_EMPTY_NAME))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verifyZeroInteractions(termsBlacklistService);
    }

    // ---------------------------------- Test Create Terms Blacklist begin -----------------------------------------------------------------//

    // ---------------------------------- Test Update Terms Blacklist begin -----------------------------------------------------------------//



    // ---------------------------------- Test Update Terms Blacklist end -------------------------------------------------------------------//

    public String jsonRequest(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }
}
