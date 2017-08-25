package com.walmart.feeds.api.unit.core.service.feed;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationFormat;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.template.TemplateRepository;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.feed.FeedServiceImpl;
import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.walmart.feeds.api.core.repository.feed.model.FeedType.INVENTORY;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FeedServiceImplTest {

    @InjectMocks
    private FeedServiceImpl feedService;

    @Mock
    private FeedHistoryRepository feedHistoryRepository;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private PartnerService partnerService;

    @Mock
    private TaxonomyBlacklistRepository taxonomyBlacklistRepository;

    @Mock
    private TemplateRepository templateRepository;

    @Test(expected = UserException.class)
    public void createFeedWithNotExistentTemplate() {

        FeedEntity f = FeedEntity.builder()
                .name("Feed Teste")
                .partner(PartnerEntity.builder()
                        .slug("teste-123")
                    .build())
                .template(TemplateEntity.builder()
                        .slug("template-123")
                    .build())
            .build();

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(mock(PartnerEntity.class));
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        feedService.createFeed(f);

    }

    @Test(expected = UserException.class)
    public void createFeedSuccess() {

        FeedEntity f = FeedEntity.builder()
                .name("Feed Teste")
                .partner(PartnerEntity.builder()
                        .slug("teste-123")
                        .build())
                .template(TemplateEntity.builder()
                        .slug("template-123")
                        .build())
                .build();

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(mock(PartnerEntity.class));
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);
        when(feedHistoryRepository.save(any(FeedHistory.class))).thenReturn(mock(FeedHistory.class));

        feedService.createFeed(f);

        verify(feedRepository, times(1)).saveAndFlush(f);
        verify(feedHistoryRepository, times(1)).save(any(FeedHistory.class));

    }

    @Test
    public void testCreateFeedWithoutTaxonomyBlacklist() {

        FeedEntity feedEntity = createFeedEntityWithoutTaxonomyBlacklist();

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);

        this.feedService.createFeed(feedEntity);

        verify(feedRepository).findBySlug(anyString());
        verify(feedRepository).saveAndFlush(Mockito.any(FeedEntity.class));
        verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
    }

    @Test(expected = InconsistentEntityException.class)
    public void createFeedWhenPartnerIsNull() {

        FeedEntity f = FeedEntity.builder()
                .name("Feed Teste")
                .partner(null)
            .build();

        feedService.createFeed(f);

    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void createFeedWhenFeedAlreadyExists() throws Exception {
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(new FeedEntity()));

        feedService.createFeed(createFeedEntity());
    }

    @Test(expected = UserException.class)
    public void createFeedWithNonExistentTaxonomyBlacklist() {


        FeedEntity f = FeedEntity.builder()
                .name("Feed Teste")
                .partner(PartnerEntity.builder()
                        .slug("teste-123")
                        .build())
                .taxonomyBlacklist(TaxonomyBlacklistEntity.builder()
                        .slug("taxonomy-nonexistent")
                        .build())
                .template(TemplateEntity.builder()
                        .slug("template-123")
                        .build())
                .build();

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(mock(PartnerEntity.class));
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.of(mock(TemplateEntity.class)));
        when(taxonomyBlacklistRepository.findBySlug("taxonomy-nonexistent")).thenReturn(Optional.empty());
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);

        feedService.createFeed(f);

    }

    @Test
    public void testUpdateFeed() {

        FeedEntity feedEntity = createFeedEntity();
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(taxonomyBlacklistRepository.findBySlug(feedEntity.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.of(feedEntity.getTaxonomyBlacklist()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);

        this.feedService.updateFeed(feedEntity);

        verify(feedRepository).findBySlug(anyString());
        verify(feedRepository).saveAndFlush(Mockito.any(FeedEntity.class));
        verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
    }

    @Test
    public void testUpdateFeedWithoutTaxonomyBlacklist() {

        FeedEntity feedEntity = createFeedEntityWithoutTaxonomyBlacklist();

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);

        this.feedService.updateFeed(feedEntity);

        verify(feedRepository).findBySlug(anyString());
        verify(feedRepository).saveAndFlush(Mockito.any(FeedEntity.class));
        verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdateFeedWhenOcurrsConflict() throws EntityNotFoundException {

        FeedEntity feedEntityUpdateName = createFeedEntityUpdateName();
        FeedEntity existentFeed = createFeedEntity();

        when(partnerService.findBySlug(anyString())).thenReturn(feedEntityUpdateName.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(existentFeed));

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
                .taxonomyBlacklist(TaxonomyBlacklistEntity.builder()
                        .slug("blacklist-test")
                        .build())
                .template(templateEntity)
                .notificationFormat(FeedNotificationFormat.JSON)
                .notificationMethod(FeedNotificationMethod.FILE)
                .type(INVENTORY).build();
        return to;
    }

    private FeedEntity createFeedEntityWithoutTaxonomyBlacklist() {
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
                .taxonomyBlacklist(TaxonomyBlacklistEntity.builder()
                        .slug("blacklist-test")
                        .build())
                .template(templateEntity)
                .notificationFormat(FeedNotificationFormat.JSON)
                .notificationMethod(FeedNotificationMethod.FILE)
                .type(INVENTORY).build();
        return to;
    }

}