package com.walmart.feeds.api.unit.resources.blacklist.taxonomy;


import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistService;
import com.walmart.feeds.api.core.utils.MapperUtil;
import com.walmart.feeds.api.resources.blacklist.TaxonomyBlackListController;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity");

        when(taxonomyBlacklistService.create(any(TaxonomyBlacklistEntity.class))).thenReturn(entity);
        mockMvc.perform(post(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY).contentType(MediaType.APPLICATION_JSON)
                .content(MapperUtil.getMapsAsJson(entity))).andExpect(status().isCreated());
    }
    @Test
    public void testTaxonomyBlacklistExceptionCreation() throws Exception {
        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity");

        when(taxonomyBlacklistService.create(any(TaxonomyBlacklistEntity.class))).thenThrow(EntityAlreadyExistsException.class);
        mockMvc.perform(post(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY).contentType(MediaType.APPLICATION_JSON)
                .content(MapperUtil.getMapsAsJson(entity))).andExpect(status().isConflict());
    }

    @Test
    public void testUpdateBlacklist() throws Exception {
        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity");

        doNothing().when(taxonomyBlacklistService).update(any(TaxonomyBlacklistEntity.class));

        mockMvc.perform(put(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY).contentType(MediaType.APPLICATION_JSON)
                .content(MapperUtil.getMapsAsJson(entity))).andExpect(status().isOk());

    }

    @Test
    public void testUpdateInvalidBlackList() throws Exception {

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity");

        doThrow(EntityNotFoundException.class).when(taxonomyBlacklistService).update(any(TaxonomyBlacklistEntity.class));

        mockMvc.perform(put(TaxonomyBlackListController.V1_BLACKLIST_TAXONOMY).contentType(MediaType.APPLICATION_JSON)
                .content(MapperUtil.getMapsAsJson(entity))).andExpect(status().isNotFound());


    }

}
