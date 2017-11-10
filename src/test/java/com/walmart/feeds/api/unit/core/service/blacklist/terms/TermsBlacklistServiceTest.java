package com.walmart.feeds.api.unit.core.service.blacklist.terms;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityInUseException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistory;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TermsBlacklistService;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TermsBlacklistServiceImpl;
import com.walmart.feeds.api.unit.core.service.feed.FeedEntityTemplateLoader;
import com.walmart.feeds.api.unit.resources.blacklist.terms.TermsBlackListTemplateLoader;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TermsBlacklistServiceTest {

    @Mock
    private TermsBlackListRepository termsBlackListRepository;

    @Mock
    private TermsBlacklistHistoryRepository termsBlacklistHistoryRepository;

    @Mock
    private FeedRepository feedRepository;

    @InjectMocks
    private TermsBlacklistService termsBlacklistService = new TermsBlacklistServiceImpl();

    @BeforeClass
    public static void setUp() {

        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.resources.blacklist.terms");
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.core.service.feed");
        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.core.service.blacklist.terms");
    }

    @Test
    @SneakyThrows
    public void testSaveTermsBlackList() {

        TermsBlacklistEntity termsBlacklistEntity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST);

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(termsBlackListRepository.saveAndFlush(any(TermsBlacklistEntity.class))).thenReturn(termsBlacklistEntity);

        termsBlacklistService.saveTermsBlacklist(Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST));

        verify(termsBlackListRepository, Mockito.times(1)).findBySlug(anyString());
        verify(termsBlackListRepository, Mockito.times(1)).saveAndFlush(any(TermsBlacklistEntity.class));
        verify(termsBlacklistHistoryRepository, Mockito.times(1)).save(any(TermsBlacklistHistory.class));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testTermsBlackListDuplicatedConstraint() {

        TermsBlacklistEntity termsBlacklistEntity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST);

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(termsBlacklistEntity));

        termsBlacklistService.saveTermsBlacklist(termsBlacklistEntity);
    }

    @Test
    public void testUpdateTermsBlackList() {

        TermsBlacklistEntity termsBlacklistEntity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST);

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(termsBlacklistEntity));
        when(termsBlackListRepository.saveAndFlush(any(TermsBlacklistEntity.class))).thenReturn(termsBlacklistEntity);

        termsBlacklistService.updateTermsBlacklist(termsBlacklistEntity);

        verify(termsBlackListRepository, Mockito.times(1)).findBySlug(anyString());
        verify(termsBlackListRepository, Mockito.times(1)).saveAndFlush(any(TermsBlacklistEntity.class));
        verify(termsBlacklistHistoryRepository, Mockito.times(1)).save(any(TermsBlacklistHistory.class));
    }

    @Test
    @SneakyThrows
    public void testUpdateTermsBlackListWhenOccursConflict() {

        TermsBlacklistEntity termsBlacklistEntity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST);

        TermsBlacklistEntity termsBlacklist = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST_UPDATE_NAME);

        // Return a existent terms blacklist
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(termsBlacklistEntity));

        try {
            termsBlacklistService.updateTermsBlacklist(termsBlacklist);
            fail("Expected EntityAlreadyExistsException");
        } catch (EntityAlreadyExistsException e) {
            verify(termsBlackListRepository, times(0)).saveAndFlush(any(TermsBlacklistEntity.class));
            verify(termsBlacklistHistoryRepository, never()).save(any(TermsBlacklistHistory.class));
        }
    }

    @Test
    public void testUpdateTermsBlackListNonexistentEntity() {

        TermsBlacklistEntity termsBlacklistEntity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST);

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        try {

            termsBlacklistService.updateTermsBlacklist(termsBlacklistEntity);
            Assert.fail("Expected EntityNotFoundException");

        } catch (EntityNotFoundException e) {

            verify(termsBlackListRepository).findBySlug(anyString());
            verify(termsBlackListRepository, Mockito.times(0)).saveAndFlush(any(TermsBlacklistEntity.class));
            Mockito.verifyNoMoreInteractions(termsBlacklistHistoryRepository);
        }
    }

    @Test
    public void testTermsBlackListFindBySlugRepository() {

        TermsBlacklistEntity termsBlacklistEntity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST);

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(termsBlacklistEntity));

        termsBlackListRepository.findBySlug(anyString());

        verify(termsBlackListRepository).findBySlug(anyString());
    }

    @Test
    public void testTermsBlackListFindBySlugService() {

        TermsBlacklistEntity termsBlacklistEntity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlacklistServiceTemplateLoader.TERMS_BLACK_LIST);

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(termsBlacklistEntity));

        termsBlacklistService.findBySlug(anyString());

        verify(termsBlackListRepository).findBySlug(anyString());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testTermsBlackListFindBySlugNotFoundService() throws Exception {

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        termsBlacklistService.findBySlug(anyString());
    }

    @Test
    public void testDeleteTermsBlacklistSuccess() {

        TermsBlacklistEntity entity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlackListTemplateLoader.TERMS_BLACKLIST_REQUEST_VALID);
        when(termsBlackListRepository.findBySlug("facebook-terms-blacklist")).thenReturn(Optional.of(entity));

        when(feedRepository.findByTermsBlacklist(any(TermsBlacklistEntity.class))).thenReturn(new ArrayList<>());

        this.termsBlacklistService.deleteTermsBlacklist("facebook-terms-blacklist");

        verify(termsBlackListRepository, times(1)).delete(entity);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteTermsBlacklistNotFound() {

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        termsBlacklistService.deleteTermsBlacklist(anyString());
    }

    @Test(expected = EntityInUseException.class)
    public void testDeleteTermsBlacklistIsInUseByFeed() {

        TermsBlacklistEntity entity = Fixture.from(TermsBlacklistEntity.class).gimme(TermsBlackListTemplateLoader.TERMS_BLACKLIST_REQUEST_VALID);
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(entity));

        FeedEntity feed = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);
        when(feedRepository.findByTermsBlacklist(any(TermsBlacklistEntity.class))).thenReturn(Arrays.asList(feed));

        this.termsBlacklistService.deleteTermsBlacklist(entity.getSlug());
    }

    @Test
    public void testFindAllTermsBlacklistEntitySuccess() {

        when(termsBlackListRepository.findAll()).thenReturn(Fixture.from(TermsBlacklistEntity.class).gimme(2, TermsBlackListTemplateLoader.TERMS_BLACKLIST_REQUEST_VALID));

        List<TermsBlacklistEntity> termsBlacklistEntities = termsBlacklistService.findAllTermsBlacklistEntity();

        assertFalse(termsBlacklistEntities.isEmpty());
        assertEquals(2, termsBlacklistEntities.size());

    }
}
