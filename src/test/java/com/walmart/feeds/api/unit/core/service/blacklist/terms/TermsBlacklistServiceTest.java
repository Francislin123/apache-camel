package com.walmart.feeds.api.unit.core.service.blacklist.terms;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistory;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistoryRepository;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TermsBlacklistService;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TermsBlacklistServiceImpl;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class TermsBlacklistServiceTest {

    @Mock
    private TermsBlackListRepository termsBlackListRepository;

    @Mock
    private TermsBlacklistHistoryRepository termsBlacklistHistoryRepository;

    @InjectMocks
    private TermsBlacklistService termsBlacklistService = new TermsBlacklistServiceImpl();

    @Before
    public void init() {
    }

    @Test
    @SneakyThrows
    public void testSaveTermsBlackList() {

        TermsBlacklistEntity termsBlackList = createTermsBlacklist();

        Mockito.when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        Mockito.when(termsBlackListRepository.saveAndFlush(any(TermsBlacklistEntity.class))).thenReturn(createTermsBlacklist());

        termsBlacklistService.saveTermsBlacklist(createTermsBlacklist());

        Mockito.verify(termsBlackListRepository, Mockito.times(1)).findBySlug(anyString());
        Mockito.verify(termsBlackListRepository, Mockito.times(1)).saveAndFlush(any(TermsBlacklistEntity.class));
        Mockito.verify(termsBlacklistHistoryRepository, Mockito.times(1)).save(any(TermsBlacklistHistory.class));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testTermsBlackListDuplicatedConstraint() {

        Mockito.when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(createTermsBlacklist()));

        termsBlacklistService.saveTermsBlacklist(createTermsBlacklist());
    }

    @Test
    public void testUpdateTermsBlackList() {

        TermsBlacklistEntity termsBlacklist = createTermsBlacklist();

        Mockito.when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(termsBlacklist));
        Mockito.when(termsBlackListRepository.saveAndFlush(any(TermsBlacklistEntity.class))).thenReturn(createTermsBlacklist());

        termsBlacklistService.updateTermsBlacklist(termsBlacklist);

        Mockito.verify(termsBlackListRepository, Mockito.times(1)).findBySlug(anyString());
        Mockito.verify(termsBlackListRepository, Mockito.times(1)).saveAndFlush(any(TermsBlacklistEntity.class));
        Mockito.verify(termsBlacklistHistoryRepository, Mockito.times(1)).save(any(TermsBlacklistHistory.class));
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
}
