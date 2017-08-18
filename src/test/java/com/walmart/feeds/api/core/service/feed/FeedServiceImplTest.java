package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationFormat;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.template.TemplateRepository;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
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
import static org.mockito.Mockito.*;

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

    @Mock
    private TemplateRepository templateRepository;

    @Test(expected = EntityNotFoundException.class)
    public void createFeedWhenPartnerNoExists() throws Exception {
        when(partnerRepository.findActiveBySlug(anyString())).thenReturn(Optional.empty());
        when(repository.findBySlug(anyString())).thenReturn(Optional.empty());

        feedService.createFeed(createFeedEntity());
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void createFeedWhenFeedAlreadyExists() throws Exception {
        when(repository.findBySlug(anyString())).thenReturn(Optional.of(new FeedEntity()));

        feedService.createFeed(createFeedEntity());
    }

    @Test
    public void testUpdateFeed() throws EntityNotFoundException {

        FeedEntity feedEntity = createFeedEntity();
        when(partnerRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity.getPartner()));
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(repository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(repository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);

        this.feedService.updateFeed(feedEntity);

        verify(repository).findBySlug(anyString());
        verify(repository).saveAndFlush(Mockito.any(FeedEntity.class));
        verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdateFeedWhenOcurrsConflict() throws EntityNotFoundException {

        FeedEntity feedEntityUpdateName = createFeedEntityUpdateName();
        FeedEntity existentFeed = createFeedEntity();

        when(partnerRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntityUpdateName.getPartner()));
        when(repository.findBySlug(anyString())).thenReturn(Optional.of(existentFeed));

        this.feedService.updateFeed(feedEntityUpdateName);

    }

    private FeedEntity createFeedEntity() {
        PartnerEntity partner = PartnerEntity.builder()
                .slug("teste123")
                .build();
        TemplateEntity templateEntity = TemplateEntity.builder()
                .slug("template")
                .build();
        FeedEntity to = FeedEntity.builder()
                .name("Big")
                .slug("big")
                .active(true)
                .partner(partner)
                .template(templateEntity)
                .notificationFormat(FeedNotificationFormat.JSON)
                .notificationMethod(FeedNotificationMethod.FILE)
                .type(INVENTORY).build();
        return to;
    }

    private FeedEntity createFeedEntityUpdateName() {
        PartnerEntity partner = PartnerEntity.builder()
                .slug("teste123")
                .build();
        TemplateEntity templateEntity = TemplateEntity.builder()
                .slug("template")
                .build();
        FeedEntity to = FeedEntity.builder()
                .name("Big")
                .slug("partner-teste")
                .active(true)
                .partner(partner)
                .template(templateEntity)
                .notificationFormat(FeedNotificationFormat.JSON)
                .notificationMethod(FeedNotificationMethod.FILE)
                .type(INVENTORY).build();
        return to;
    }
}