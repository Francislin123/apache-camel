package com.walmart.feeds.api.unit.core.service.feed;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationFormat;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.core.repository.fields.FieldsMappingRepository;
import com.walmart.feeds.api.core.repository.fields.model.FieldsMappingEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
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
    private TemplateRepository templateRepository;

    @Mock
    private PartnerTaxonomyRepository partnerTaxonomyRepository;

    @Mock
    private FieldsMappingRepository fieldsMappingRepository;

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
    public void updateFeedWhenTemplateDoNotExists() {
        FeedEntity feedEntity = createFeedEntity();
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(templateRepository.findBySlug("template")).thenReturn(Optional.empty());

        this.feedService.updateFeed(feedEntity);
    }

    @Test(expected = UserException.class)
    public void updateFeedWhenPartnerTaxonomyDoNotExists() {
        FeedEntity feedEntity = createFeedEntity();
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.empty());

        this.feedService.updateFeed(feedEntity);
    }

    @Test(expected = UserException.class)
    public void updateFeedWhenFieldMappingDoNotExists() {
        FeedEntity feedEntity = createFeedEntity();
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.of(feedEntity.getPartnerTaxonomy()));
        when(fieldsMappingRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        this.feedService.updateFeed(feedEntity);
    }


    @Test
    public void createFeedSuccess() {

        FeedEntity f = FeedEntity.builder()
                .name("Feed Teste")
                .partner(PartnerEntity.builder()
                        .slug("teste-123")
                        .build())
                .template(TemplateEntity.builder()
                        .slug("template-123")
                        .build())
                .partnerTaxonomy(PartnerTaxonomyEntity.builder()
                        .slug("taxonomy-google")
                        .build())
                .fieldsMapping(FieldsMappingEntity.builder()
                        .slug("fields-mapping-teste")
                        .build())
                .notificationFormat(FeedNotificationFormat.JSON)
                .notificationMethod(FeedNotificationMethod.API)
                .build();

        PartnerEntity mockPartner = mock(PartnerEntity.class);

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(mockPartner);
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.of(f.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(mockPartner))).thenReturn(Optional.of(f.getPartnerTaxonomy()));
        when(fieldsMappingRepository.findBySlug(anyString())).thenReturn(Optional.of(f.getFieldsMapping()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);
        when(feedHistoryRepository.save(any(FeedHistory.class))).thenReturn(mock(FeedHistory.class));

        feedService.createFeed(f);

        verify(feedRepository, times(1)).saveAndFlush(any(FeedEntity.class));
        verify(feedHistoryRepository, times(1)).save(any(FeedHistory.class));

    }

    @Test(expected = UserException.class)
    public void createFeedWhenTaxonomyNotFound() {

        FeedEntity feedEntity = createFeedEntity();
        when(partnerService.findActiveBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.empty());

        this.feedService.createFeed(feedEntity);
    }

    @Test(expected = UserException.class)
    public void CreateFeedWhenFieldMappingDoNotExists() {

        FeedEntity feedEntity = createFeedEntity();
        when(partnerService.findActiveBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.of(feedEntity.getPartnerTaxonomy()));
        when(fieldsMappingRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        this.feedService.createFeed(feedEntity);
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

    @Test
    public void testUpdateFeed() throws EntityNotFoundException {

        FeedEntity feedEntity = createFeedEntity();
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.of(feedEntity.getPartnerTaxonomy()));
        when(fieldsMappingRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity.getFieldsMapping()));
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
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
        PartnerTaxonomyEntity partnerTaxonomy = PartnerTaxonomyEntity.builder()
                .slug("taxonomy-123")
            .build();
        FieldsMappingEntity fieldMapping = FieldsMappingEntity.builder()
                .slug("field-mapping-123")
            .build();
        FeedEntity to = FeedEntity.builder()
                .name("Big")
                .slug("big")
                .active(true)
                .partner(partner)
                .template(templateEntity)
                .partnerTaxonomy(partnerTaxonomy)
                .fieldsMapping(fieldMapping)
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
                .partnerTaxonomy(PartnerTaxonomyEntity.builder()
                        .slug("google-taxonomy")
                        .build())
                .fieldsMapping(FieldsMappingEntity.builder()
                        .slug("fields-mapping-123")
                        .build())
                .notificationFormat(FeedNotificationFormat.JSON)
                .notificationMethod(FeedNotificationMethod.FILE)
                .type(INVENTORY).build();
        return to;
    }
}