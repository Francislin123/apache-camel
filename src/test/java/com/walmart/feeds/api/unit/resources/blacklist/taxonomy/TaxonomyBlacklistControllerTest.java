package com.walmart.feeds.api.unit.resources.blacklist.taxonomy;


import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistService;
import com.walmart.feeds.api.core.utils.MapperUtil;
import com.walmart.feeds.api.resources.blacklist.TaxonomyBlackListController;
import com.walmart.feeds.api.resources.blacklist.request.TaxonomyBlacklistRequest;
import com.walmart.feeds.api.resources.infrastructure.FeedsAdminAPIExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TaxonomyBlacklistControllerTest {

    private static MockMvc mockMvc;

    @InjectMocks
    private TaxonomyBlackListController taxonomyBlackListController;

    @Mock
    private TaxonomyBlacklistService taxonomyBlacklistService;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(taxonomyBlackListController)
                .setControllerAdvice(new FeedsAdminAPIExceptionHandler())
                .build();

        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.taxonomy");
    }

    @Test
    public void testTaxonomyBlacklistCreation() throws Exception {

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);

        when(taxonomyBlacklistService.create(any(TaxonomyBlacklistEntity.class))).thenReturn(entity);
        mockMvc.perform(post(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY).contentType(MediaType.APPLICATION_JSON)
                .content(MapperUtil.getMapsAsJson(Fixture.from(TaxonomyBlacklistRequest.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST_REQUEST))))
                .andExpect(status().isCreated());
    }

    @Test
    public void testTaxonomyBlacklistExceptionCreation() throws Exception {
        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);

        when(taxonomyBlacklistService.create(any(TaxonomyBlacklistEntity.class))).thenThrow(EntityAlreadyExistsException.class);
        mockMvc.perform(post(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY).contentType(MediaType.APPLICATION_JSON)
                .content(MapperUtil.getMapsAsJson(Fixture.from(TaxonomyBlacklistRequest.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST_REQUEST))))
                .andExpect(status().isConflict());
    }

    @Test
    public void testUpdateBlacklist() throws Exception {
        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);

        doNothing().when(taxonomyBlacklistService).update(any(TaxonomyBlacklistEntity.class));

        mockMvc.perform(put(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY+"/slug").contentType(MediaType.APPLICATION_JSON)
                .content(MapperUtil.getMapsAsJson(Fixture.from(TaxonomyBlacklistRequest.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST_REQUEST))))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdateInvalidBlackList() throws Exception {

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);

        doThrow(EntityNotFoundException.class).when(taxonomyBlacklistService).update(any(TaxonomyBlacklistEntity.class));

        mockMvc.perform(put(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY+ "/anySlug").contentType(MediaType.APPLICATION_JSON)
                .content(MapperUtil.getMapsAsJson(Fixture.from(TaxonomyBlacklistRequest.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST_REQUEST))))
                .andExpect(status().isNotFound());


    }

    @Test
    public void testFetchTaxonomyBlackListBySlug() throws Exception {

        String slug = "any-name";
        when(taxonomyBlacklistService.find(slug)).thenReturn(Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST));

        mockMvc.perform(get(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY + "/" + slug)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").value("any name"));

    }

    @Test
    public void testFetchTaxonomyBlackListBySlugNotFound() throws Exception {

        String slug = "any-name";
        when(taxonomyBlacklistService.find(slug)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY + "/" + slug)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testFetchAll() throws Exception {

        when(taxonomyBlacklistService.findAll())
                .thenReturn(Fixture.from(TaxonomyBlacklistEntity.class).gimme(2, TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST));

        mockMvc.perform(get(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.result").isNotEmpty());

    }

    @Test
    public void testFetchAllWhenOccursRuntimeException() throws Exception {

        when(taxonomyBlacklistService.findAll()).thenThrow(RuntimeException.class);

        mockMvc.perform(get(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isInternalServerError());

    }

    @Test
    public void deleteTaxonomyBlacklist() throws Exception {

        doNothing().when(taxonomyBlacklistService).deleteBySlug("anySlug");

        mockMvc.perform(delete(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY+ "/anySlug")).andExpect(status().isNoContent());
    }

    @Test
    public void callDeleteAndReturnNotFoundError() throws Exception {

        doThrow(EntityNotFoundException.class).when(taxonomyBlacklistService).deleteBySlug("invalidSlug");

        mockMvc.perform(delete(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY+ "/invalidSlug")).andExpect(status().isNotFound());
    }

}
