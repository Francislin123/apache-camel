package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.Feed;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.walmart.feeds.api.core.repository.feed.model.FeedType.INVENTORY;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedServiceImplTest {

    @InjectMocks
    private FeedServiceImpl feedService;

    @Mock
    private FeedHistoryRepository feedHistoryRepository;

    @Mock
    private FeedRepository repository;

    @Mock
    private PartnerRepository partnerRepository;

    @Test(expected = NotFoundException.class)
    public void createFeedWhenPartnerNoExists() throws Exception {
        when(partnerRepository.findByReference(anyString())).thenReturn(Optional.empty());
        feedService.createFeed(new FeedTO());
    }

    public void testUpdatePartner() throws com.walmart.feeds.api.core.exceptions.NotFoundException {
        try {
            when(repository.findByReference(anyString())) .thenReturn(Optional.of(new Feed()));

            FeedTO request = new FeedTO();

            this.feedService.updateFeed(request);

            verify(repository).findByReference(anyString());
            verify(repository).save(Mockito.any(Feed.class));
            verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
        } catch (Exception e) {
            fail("Exception should not have been fired!");
        }
    }

    @Test
    public void testUpdateInexistentPartnerShouldThrowNotFoundException() throws com.walmart.feeds.api.core.exceptions.NotFoundException {
        when(repository.findByReference(anyString())).thenReturn(Optional.empty());
        FeedTO feedTO = createFeedTO();
        this.feedService.updateFeed(feedTO);
        verify(repository).findByReference(anyString());
        verify(repository, times(0)).save(Mockito.any(Feed.class));
        verify(feedHistoryRepository, times(0)).save(Mockito.any(FeedHistory.class));
    }

    private FeedTO createFeedTO() {
        PartnerTO partnerTO = new PartnerTO();
        FeedTO to = new FeedTO();
        to.setName("Big");
        to.setReference("partner-teste");
        to.setActive(true);
        to.setPartner(partnerTO);
        to.setType(INVENTORY);
        return to;
    }
}