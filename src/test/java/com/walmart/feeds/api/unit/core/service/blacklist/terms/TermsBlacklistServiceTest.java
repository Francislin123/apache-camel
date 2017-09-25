package com.walmart.feeds.api.unit.core.service.blacklist.terms;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistory;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TermsBlacklistService;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TermsBlacklistServiceImpl;
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

import java.util.*;

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
    }

    @Test
    @SneakyThrows
    public void testSaveTermsBlackList() {

        TermsBlacklistEntity termsBlackList = createTermsBlacklist();

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(termsBlackListRepository.saveAndFlush(any(TermsBlacklistEntity.class))).thenReturn(createTermsBlacklist());

        termsBlacklistService.saveTermsBlacklist(createTermsBlacklist());

        verify(termsBlackListRepository, Mockito.times(1)).findBySlug(anyString());
        verify(termsBlackListRepository, Mockito.times(1)).saveAndFlush(any(TermsBlacklistEntity.class));
        verify(termsBlacklistHistoryRepository, Mockito.times(1)).save(any(TermsBlacklistHistory.class));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testTermsBlackListDuplicatedConstraint() {

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(createTermsBlacklist()));

        termsBlacklistService.saveTermsBlacklist(createTermsBlacklist());
    }

    @Test
    public void testUpdateTermsBlackList() {

        TermsBlacklistEntity termsBlacklist = createTermsBlacklist();

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(termsBlacklist));
        when(termsBlackListRepository.saveAndFlush(any(TermsBlacklistEntity.class))).thenReturn(createTermsBlacklist());

        termsBlacklistService.updateTermsBlacklist(termsBlacklist);

        verify(termsBlackListRepository, Mockito.times(1)).findBySlug(anyString());
        verify(termsBlackListRepository, Mockito.times(1)).saveAndFlush(any(TermsBlacklistEntity.class));
        verify(termsBlacklistHistoryRepository, Mockito.times(1)).save(any(TermsBlacklistHistory.class));
    }

    @Test
    @SneakyThrows
    public void testUpdateTermsBlackListWhenOccursConflict() {

        TermsBlacklistEntity termsBlacklist = createTermsBlacklistUpdateName();

        // Return a existent terms blacklist
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(createTermsBlacklist()));

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

        TermsBlacklistEntity termsBlacklist = createTermsBlacklist();

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        try {

            termsBlacklistService.updateTermsBlacklist(termsBlacklist);
            Assert.fail("Expected EntityNotFoundException");

        } catch (EntityNotFoundException e) {

            verify(termsBlackListRepository).findBySlug(anyString());
            verify(termsBlackListRepository, Mockito.times(0)).saveAndFlush(any(TermsBlacklistEntity.class));
            Mockito.verifyNoMoreInteractions(termsBlacklistHistoryRepository);
        }
    }

    @Test
    public void testTermsBlackListFindBySlugRepository() {

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(createTermsBlacklist()));

        termsBlackListRepository.findBySlug(anyString());

        verify(termsBlackListRepository).findBySlug(anyString());
    }

    @Test
    public void testTermsBlackListFindBySlugService() {

        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(createTermsBlacklist()));

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

    @Test
    public void testFindAllTermsBlacklistEntitySuccess() {

        when(termsBlackListRepository.findAll()).thenReturn(Fixture.from(TermsBlacklistEntity.class).gimme(2, TermsBlackListTemplateLoader.TERMS_BLACKLIST_REQUEST_VALID));

        List<TermsBlacklistEntity> termsBlacklistEntities = termsBlacklistService.findAllTermsBlacklistEntity();

        assertFalse(termsBlacklistEntities.isEmpty());
        assertEquals(2, termsBlacklistEntities.size());

    }

    private TermsBlacklistEntity createTermsBlacklist() {

        Set<String> list = new HashSet<>();
        list.add("bullet");

        TermsBlacklistEntity termsBlacklistEntity = TermsBlacklistEntity.builder()
                .name("Facebook-terms-blacklist")
                .slug("facebook-terms-blacklist")
                .list(list)
                .build();

        return termsBlacklistEntity;
    }

    private TermsBlacklistEntity createTermsBlacklistUpdateName() {

        Set<String> list = new HashSet<>();
        list.add("bullet");
        list.add("arms");

        TermsBlacklistEntity termsBlacklistEntity = TermsBlacklistEntity.builder()
                .name("Google-terms-blacklist")
                .slug("facebook-terms-blacklist")
                .list(list)
                .build();

        return termsBlacklistEntity;
    }
}
