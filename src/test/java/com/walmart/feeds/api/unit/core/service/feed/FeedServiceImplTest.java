package com.walmart.feeds.api.unit.core.service.feed;

import com.walmart.feeds.api.client.tags.TagAdmimCollectionClient;
import com.walmart.feeds.api.client.tags.TagAdminCollection;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationFormat;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
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
    private FeedRepository repository;

    @Mock
    private PartnerService partnerService;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private TagAdmimCollectionClient tagAdminCollectionClient;

    @Test(expected = UserException.class)
    public void createFeedWithNotExistentTemplate() {

        FeedEntity f = FeedEntity.builder()
                .name("Feed Teste")
                .partner(PartnerEntity.builder()
                .slug("teste-123")
                .build())
                .collectionId(7380L)
                .template(TemplateEntity.builder()
                .slug("template-123")
                .build())
            .build();

        when(repository.findBySlug(anyString())).thenReturn(Optional.empty());
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
                .collectionId(7380L)
                .template(TemplateEntity.builder()
                        .slug("template-123")
                        .build())
                .build();
        when(repository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(mock(PartnerEntity.class));
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        when(repository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);
        when(feedHistoryRepository.save(any(FeedHistory.class))).thenReturn(mock(FeedHistory.class));

        feedService.createFeed(f);

        verify(repository, times(1)).saveAndFlush(f);
        verify(feedHistoryRepository, times(1)).save(any(FeedHistory.class));

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

        TagAdminCollection tagAdminCollection = TagAdminCollection.builder().status("ACTIVE").build();

        when(tagAdminCollectionClient.findById(7380L)).thenReturn(tagAdminCollection);
        when(repository.findBySlug(anyString())).thenReturn(Optional.of(new FeedEntity()));

        feedService.createFeed(createFeedEntity());
    }

    @Test
    public void testUpdateFeed() throws EntityNotFoundException {

        FeedEntity feedEntity = createFeedEntity();

        TagAdminCollection tagAdminCollection = TagAdminCollection.builder().status("ACTIVE").build();

        when(tagAdminCollectionClient.findById(7380L)).thenReturn(tagAdminCollection);
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
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
        TagAdminCollection tagAdminCollection = TagAdminCollection.builder().status("ACTIVE").build();

        when(tagAdminCollectionClient.findById(7380L)).thenReturn(tagAdminCollection);
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntityUpdateName.getPartner());
        when(repository.findBySlug(anyString())).thenReturn(Optional.of(existentFeed));

        this.feedService.updateFeed(feedEntityUpdateName);

    }

    private FeedEntity createFeedEntity() {

        PartnerEntity partner = PartnerEntity.builder().slug("teste123").build();
        TemplateEntity templateEntity = TemplateEntity.builder().slug("template").build();

        FeedEntity to = FeedEntity.builder()
                .name("Big")
                .slug("big")
                .active(true)
                .collectionId(7380L)
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
                .collectionId(7380L)
                .partner(partner)
                .template(templateEntity)
                .notificationFormat(FeedNotificationFormat.JSON)
                .notificationMethod(FeedNotificationMethod.FILE)
                .type(INVENTORY).build();
        return to;
    }

    private TagAdminCollection createTagAdminUpdate() {
        TagAdminCollection admim = TagAdminCollection.builder()
                .id(7380)
                .version(1)
                .name("Tags")
                .domain("Tags")
                .status("ACTIVE").build();
        return admim;
    }
}