package com.walmart.feeds.api.unit.core.service.blacklist.taxonomy;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.persistence.elasticsearch.ElasticSearchService;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistHistoryRepository;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistHistory;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistMapping;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyOwner;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistService;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistServiceImpl;
import com.walmart.feeds.api.unit.resources.blacklist.taxonomy.TaxonomyBlacklistTemplateLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(MockitoJUnitRunner.class)
public class TaxonomyBlacklistServiceTest {

    @InjectMocks
    private TaxonomyBlacklistService taxonomyBlacklistService = new TaxonomyBlacklistServiceImpl();

    @Mock
    private TaxonomyBlacklistRepository taxonomyBlacklistRepository;

    @Mock
    private TaxonomyBlacklistHistoryRepository taxonomyBlacklistHistoryRepository;

    @Mock
    private ElasticSearchService elasticSearchService;

    @Before
    public void init() {

        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.taxonomy");

        when(elasticSearchService.validateWalmartTaxonomy("Eletrônicos > TVs")).thenReturn(true);

    }

    @Test
    public void createTaxonomyBlacklistTest(){

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);

        when(taxonomyBlacklistRepository.saveAndFlush(entity)).thenReturn(entity);

        when(taxonomyBlacklistHistoryRepository.save(any(TaxonomyBlacklistHistory.class))).thenReturn(any(TaxonomyBlacklistHistory.class));

        TaxonomyBlacklistEntity entitySaved = this.taxonomyBlacklistService.create(entity);

        assertEquals(entity.getSlug(), entitySaved.getSlug());
    }

    @Test
    public void createTaxonomyBlacklistTestWithInvalidWalmartTaxonomy(){

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TB_INVALID_WALMART_TAXONOMY);

        when(taxonomyBlacklistRepository.saveAndFlush(entity)).thenReturn(entity);

        when(taxonomyBlacklistHistoryRepository.save(any(TaxonomyBlacklistHistory.class))).thenReturn(any(TaxonomyBlacklistHistory.class));

        try {
            TaxonomyBlacklistEntity entitySaved = this.taxonomyBlacklistService.create(entity);
            fail("A UserException was expected!");
        } catch (UserException e) {
            assertTrue(e.getMessage().contains("The following walmart taxonomies aren't in walmart structure"));
        }

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void duplicatedEntityTest(){

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);

        when(taxonomyBlacklistRepository.saveAndFlush(entity)).thenThrow(EntityAlreadyExistsException.class);

        this.taxonomyBlacklistService.create(entity);

    }

    @Test
    public void updateBlackList(){
        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);

        when(taxonomyBlacklistRepository.findBySlug(entity.getSlug())).thenReturn(Optional.of(entity));

        when(taxonomyBlacklistRepository.saveAndFlush(any(TaxonomyBlacklistEntity.class))).thenReturn(entity);

        when(taxonomyBlacklistHistoryRepository.save(any(TaxonomyBlacklistHistory.class))).thenReturn(any(TaxonomyBlacklistHistory.class));

        this.taxonomyBlacklistService.update(entity);

        verify(taxonomyBlacklistRepository, times(1)).saveAndFlush(any(TaxonomyBlacklistEntity.class));

    }

    @Test
    public void testFind() {

        when(taxonomyBlacklistRepository.findBySlug("any-name")).thenReturn(Optional.of(Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST)));

        TaxonomyBlacklistEntity entity = taxonomyBlacklistService.find("any-name");

        assertEquals("any name", entity.getName());

    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindWhenItNotExists() {

        when(taxonomyBlacklistRepository.findBySlug("any-name")).thenReturn(Optional.empty());

        TaxonomyBlacklistEntity entity = taxonomyBlacklistService.find("any-name");

    }

    @Test
    public void testFindAll() {

        when(taxonomyBlacklistRepository.findAll()).thenReturn(Fixture.from(TaxonomyBlacklistEntity.class).gimme(2,TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST));

        List<TaxonomyBlacklistEntity> taxonomyBlacklists = taxonomyBlacklistService.findAll();

        assertFalse(taxonomyBlacklists.isEmpty());
        assertEquals(2, taxonomyBlacklists.size());

    }

    @Test
    public void testDelete(){

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme(TaxonomyBlacklistTemplateLoader.TAXONOMY_BLACKLIST);

        when(taxonomyBlacklistRepository.findBySlug("any-name")).thenReturn(Optional.of(entity));
        doNothing().when(taxonomyBlacklistRepository).delete(entity);

        this.taxonomyBlacklistService.deleteBySlug("any-name");

        verify(taxonomyBlacklistRepository, times(1)).delete(entity);

    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteInvalidSlug(){

        doThrow(EntityNotFoundException.class).when(taxonomyBlacklistRepository).findBySlug("invalidSlug");

        this.taxonomyBlacklistService.deleteBySlug("invalidSlug");
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testTaxonomyBlacklistConflict(){
        TaxonomyBlacklistEntity entity = TaxonomyBlacklistEntity.builder()
                .name("already-existent-slug").slug("Slug-different-from-name").build();

        when(taxonomyBlacklistRepository.findBySlug("already-existent-slug")).thenReturn(Optional.of(entity));

        this.taxonomyBlacklistService.update(entity);

    }

    @Test(expected = UserException.class)
    public void testInvalidWalmartTaxonomy(){
        TaxonomyBlacklistMapping taxMap = TaxonomyBlacklistMapping.builder().owner(TaxonomyOwner.WALMART)
                .taxonomy("Invalid Taxonomy").build();
        Set<TaxonomyBlacklistMapping> set = new HashSet<>();
        set.add(taxMap);

        TaxonomyBlacklistEntity entity = TaxonomyBlacklistEntity.builder().list(set).build();

        when(elasticSearchService.validateWalmartTaxonomy("Invalid Taxonomy")).thenReturn(false);

        this.taxonomyBlacklistService.create(entity);
    }

}
