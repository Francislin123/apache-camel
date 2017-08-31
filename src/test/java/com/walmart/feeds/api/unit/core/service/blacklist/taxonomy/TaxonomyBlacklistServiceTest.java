package com.walmart.feeds.api.unit.core.service.blacklist.taxonomy;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistHistoryRepository;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistHistory;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistService;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaxonomyBlacklistServiceTest {

    @InjectMocks
    private TaxonomyBlacklistService taxonomyBlacklistService = new TaxonomyBlacklistServiceImpl();

    @Mock
    private TaxonomyBlacklistRepository taxonomyBlacklistRepository;

    @Mock
    private TaxonomyBlacklistHistoryRepository taxonomyBlacklistHistoryRepository;

    @Before
    public void init() {

        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.taxonomy");
    }

    @Test
    public void createTaxonomyBlacklistTest(){

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity");

        when(taxonomyBlacklistRepository.saveAndFlush(entity)).thenReturn(entity);

        when(taxonomyBlacklistHistoryRepository.save(any(TaxonomyBlacklistHistory.class))).thenReturn(any(TaxonomyBlacklistHistory.class));

        TaxonomyBlacklistEntity entitySaved = this.taxonomyBlacklistService.create(entity);

        assertEquals(entity.getSlug(), entitySaved.getSlug());
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void duplicatedEntityTest(){

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity");

        when(taxonomyBlacklistRepository.saveAndFlush(entity)).thenThrow(EntityAlreadyExistsException.class);

        this.taxonomyBlacklistService.create(entity);

    }

    @Test
    public void updateBlackList(){
        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity");

        when(taxonomyBlacklistRepository.findBySlug(entity.getSlug())).thenReturn(Optional.of(entity));

        when(taxonomyBlacklistRepository.saveAndFlush(any(TaxonomyBlacklistEntity.class))).thenReturn(entity);

        when(taxonomyBlacklistHistoryRepository.save(any(TaxonomyBlacklistHistory.class))).thenReturn(any(TaxonomyBlacklistHistory.class));

        this.taxonomyBlacklistService.update(entity);

        verify(taxonomyBlacklistRepository, times(1)).saveAndFlush(any(TaxonomyBlacklistEntity.class));

    }

    @Test
    public void testFind() {

        when(taxonomyBlacklistRepository.findBySlug("any-name")).thenReturn(Optional.of(Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity")));

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

        when(taxonomyBlacklistRepository.findAll()).thenReturn(Fixture.from(TaxonomyBlacklistEntity.class).gimme(2,"tax-bl-entity"));

        List<TaxonomyBlacklistEntity> taxonomyBlacklists = taxonomyBlacklistService.findAll();

        assertFalse(taxonomyBlacklists.isEmpty());
        assertEquals(2, taxonomyBlacklists.size());

    }

    @Test
    public void testDelete(){

        TaxonomyBlacklistEntity entity = Fixture.from(TaxonomyBlacklistEntity.class).gimme("tax-bl-entity");

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

}
