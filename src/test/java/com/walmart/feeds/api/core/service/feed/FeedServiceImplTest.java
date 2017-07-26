package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.service.feed.model.FeedTO;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedServiceImplTest {

    @InjectMocks
    private FeedServiceImpl feedService;

    @Mock
    private PartnerRepository partnerRepository;

    @Test(expected = NotFoundException.class)
    public void createFeedWhenPartnerNoExists() throws Exception {
        when(partnerRepository.findByReference(anyString())).thenReturn(Optional.empty());
        feedService.createFeed(new FeedTO());
    }

}