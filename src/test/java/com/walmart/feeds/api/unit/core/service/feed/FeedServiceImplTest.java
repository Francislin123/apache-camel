package com.walmart.feeds.api.unit.core.service.feed;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.walmart.feeds.api.client.tagadmin.TagAdminCollection;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InvalidFeedException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.notifications.SendMailService;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistRepository;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.template.TemplateRepository;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistService;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions.TaxonomyBlacklistNotFoundException;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions.TaxonomyBlacklistPartnerException;
import com.walmart.feeds.api.core.service.feed.FeedServiceImpl;
import com.walmart.feeds.api.core.service.feed.ProductCollectionService;
import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.unit.resources.partner.test.template.PartnerTemplateLoader;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

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
    private TermsBlackListRepository termsBlackListRepository;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private ProductCollectionService productCollectionService;

    @Mock
    private PartnerTaxonomyRepository partnerTaxonomyRepository;

    @Mock
    private SendMailService sendMailService;

    @Mock
    private TaxonomyBlacklistService taxonomyBlacklistService;

    @BeforeClass
    public static void setUp() {

        FixtureFactoryLoader.loadTemplates("com.walmart.feeds.api.unit.core.service.feed");
    }

    @Test(expected = UserException.class)
    public void createFeedWithNotExistentTemplate() {

        FeedEntity f = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY_NO_EXISTENT_TEMPLATE);

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(mock(PartnerEntity.class));
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.empty());

        feedService.createFeed(f);

    }

    @Test(expected = UserException.class)
    public void updateFeedWhenTemplateDoNotExists() {
        FeedEntity feedEntity = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(templateRepository.findBySlug("template")).thenReturn(Optional.empty());

        this.feedService.updateFeed(feedEntity);
    }

    @Test(expected = UserException.class)
    public void updateFeedWhenPartnerTaxonomyDoNotExists() {
        FeedEntity feedEntity = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(taxonomyBlacklistRepository.findBySlug(feedEntity.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.of(feedEntity.getTaxonomyBlacklist()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.empty());
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity.getTermsBlacklist().get(0)));

        this.feedService.updateFeed(feedEntity);
    }

    @Test
    public void createFeedSuccess() {

        FeedEntity f = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);

        PartnerEntity mockPartner = mock(PartnerEntity.class);

        doNothing().when(productCollectionService).validateCollectionExists(7380L);

        when(taxonomyBlacklistRepository.findBySlug(f.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.of(f.getTaxonomyBlacklist()));
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(f.getTermsBlacklist().get(0)));
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(mockPartner);
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.of(f.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(mockPartner))).thenReturn(Optional.of(f.getPartnerTaxonomy()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);
        when(feedHistoryRepository.save(any(FeedHistory.class))).thenReturn(mock(FeedHistory.class));

        feedService.createFeed(f);

        verify(feedRepository, times(1)).saveAndFlush(any(FeedEntity.class));
        verify(feedHistoryRepository, times(1)).save(any(FeedHistory.class));
        verify(productCollectionService, times(1)).validateCollectionExists(f.getCollectionId());

    }

    @Test
    public void testCreateFeedFailureWhenPartnerTaxonomiesOnBlacklistAreValid() {

        FeedEntity f = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);

        doNothing().when(productCollectionService).validateCollectionExists(7380L);

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(f.getPartner());
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.of(f.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(f.getPartnerTaxonomy().getSlug(), f.getPartner())).thenReturn(Optional.of(f.getPartnerTaxonomy()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);
        when(feedHistoryRepository.save(any(FeedHistory.class))).thenReturn(mock(FeedHistory.class));
        when(taxonomyBlacklistRepository.findBySlug(f.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.of(f.getTaxonomyBlacklist()));
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(f.getTermsBlacklist().get(0)));

        feedService.createFeed(f);
    }

    @Test(expected = TaxonomyBlacklistNotFoundException.class)
    public void testCreateFeedFailureWhenTaxonomyBlacklistNotExists() {

        FeedEntity f = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);

        doNothing().when(productCollectionService).validateCollectionExists(7380L);

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(f.getPartner());
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.of(f.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(f.getPartnerTaxonomy().getSlug(), f.getPartner())).thenReturn(Optional.of(f.getPartnerTaxonomy()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);
        when(feedHistoryRepository.save(any(FeedHistory.class))).thenReturn(mock(FeedHistory.class));
        when(taxonomyBlacklistRepository.findBySlug(f.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.empty());

        feedService.createFeed(f);

    }

    @Test(expected = TaxonomyBlacklistPartnerException.class)
    public void testCreateFeedFailureWhenBlacklistPartnerTaxonomyNotExists() {

        FeedEntity f = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY_WITH_INVALID_PARTNER_TAXONOMY_BLACKLIST);

        doNothing().when(productCollectionService).validateCollectionExists(7380L);

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(f.getPartner());
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.of(f.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(f.getPartnerTaxonomy().getSlug(), f.getPartner())).thenReturn(Optional.of(f.getPartnerTaxonomy()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);
        when(feedHistoryRepository.save(any(FeedHistory.class))).thenReturn(mock(FeedHistory.class));
        when(taxonomyBlacklistRepository.findBySlug(f.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.of(f.getTaxonomyBlacklist()));

        feedService.createFeed(f);
    }

    @Test
    public void testCreateFeedWithoutTaxonomyBlacklist() {

        FeedEntity feedEntity = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY_WITHOUT_TAXONOMY_BLACKLIST);

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.of(feedEntity.getPartnerTaxonomy()));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity.getTermsBlacklist().get(0)));

        this.feedService.createFeed(feedEntity);

        verify(feedRepository).findBySlug(anyString());
        verify(feedRepository).saveAndFlush(Mockito.any(FeedEntity.class));
        verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
    }

    @Test(expected = UserException.class)
    public void createFeedWhenTaxonomyNotFound() {

        FeedEntity feedEntity = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);
        when(partnerService.findActiveBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.empty());

        this.feedService.createFeed(feedEntity);
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void createFeedWhenFeedAlreadyExists() throws Exception {

        doNothing().when(productCollectionService).validateCollectionExists(7380L);
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(new FeedEntity()));

        feedService.createFeed(Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY));
    }

    @Test(expected = UserException.class)
    public void createFeedWithNonExistentTaxonomyBlacklist() {

        FeedEntity f = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(partnerService.findActiveBySlug(anyString())).thenReturn(f.getPartner());
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.of(mock(TemplateEntity.class)));
        when(taxonomyBlacklistRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(f);
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(f.getPartner()))).thenReturn(Optional.of(f.getPartnerTaxonomy()));

        feedService.createFeed(f);
    }

    @Test
    public void testUpdateFeed() {

        FeedEntity feedEntity = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);

        doNothing().when(productCollectionService).validateCollectionExists(feedEntity.getCollectionId());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(taxonomyBlacklistRepository.findBySlug(feedEntity.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.of(feedEntity.getTaxonomyBlacklist()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.of(feedEntity.getPartnerTaxonomy()));
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity.getTermsBlacklist().get(0)));

        this.feedService.updateFeed(feedEntity);

        verify(feedRepository).findBySlug(anyString());
        verify(feedRepository).saveAndFlush(Mockito.any(FeedEntity.class));
        verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
    }

    @Test
    public void testUpdateFeedWithoutTaxonomyBlacklist() {

        FeedEntity feedEntity = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY_WITHOUT_TAXONOMY_BLACKLIST);

        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(anyString(), eq(feedEntity.getPartner()))).thenReturn(Optional.of(feedEntity.getPartnerTaxonomy()));
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity.getTermsBlacklist().get(0)));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);

        this.feedService.updateFeed(feedEntity);

        verify(feedRepository).findBySlug(anyString());
        verify(feedRepository).saveAndFlush(Mockito.any(FeedEntity.class));
        verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
    }

    @Test
    public void testUpdateFeedWhenCollectionIdIsNull() throws EntityNotFoundException {

        FeedEntity feedEntity = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY_COLLECTION_ID_NULL);

        doNothing().when(productCollectionService).validateCollectionExists(feedEntity.getCollectionId());
        when(taxonomyBlacklistRepository.findBySlug(feedEntity.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.of(feedEntity.getTaxonomyBlacklist()));
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity.getTermsBlacklist().get(0)));
        when(partnerService.findBySlug(anyString())).thenReturn(feedEntity.getPartner());
        when(templateRepository.findBySlug("template")).thenReturn(Optional.of(feedEntity.getTemplate()));
        when(partnerTaxonomyRepository.findBySlugAndPartner(feedEntity.getPartnerTaxonomy().getSlug(), feedEntity.getPartner())).thenReturn(Optional.of(feedEntity.getPartnerTaxonomy()));
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);

        this.feedService.updateFeed(feedEntity);

        verify(feedRepository).findBySlug(anyString());
        verify(feedRepository).saveAndFlush(Mockito.any(FeedEntity.class));
        verify(feedHistoryRepository).save(Matchers.any(FeedHistory.class));
        verify(productCollectionService, never()).validateCollectionExists(feedEntity.getCollectionId());
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testUpdateFeedWhenOccursConflict() throws EntityNotFoundException {

        FeedEntity feedEntityUpdateName = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY_UPDATE_NAME);
        FeedEntity existentFeed = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY);

        when(partnerService.findBySlug(anyString())).thenReturn(feedEntityUpdateName.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.of(existentFeed));

        this.feedService.updateFeed(feedEntityUpdateName);
    }

    @Test
    public void testCreateFeedCollectionIdNull() {

        TemplateEntity templateEntity = TemplateEntity.builder().slug("template").build();
        FeedEntity feedEntity = Fixture.from(FeedEntity.class).gimme(FeedEntityTemplateLoader.FEED_ENTITY_COLLECTION_ID_NULL);

        when(partnerService.findActiveBySlug(feedEntity.getPartner().getSlug())).thenReturn(feedEntity.getPartner());
        when(feedRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(templateRepository.findBySlug(anyString())).thenReturn(Optional.of(templateEntity));
        when(feedRepository.saveAndFlush(any(FeedEntity.class))).thenReturn(feedEntity);
        when(partnerTaxonomyRepository.findBySlugAndPartner(feedEntity.getPartnerTaxonomy().getSlug(), feedEntity.getPartner())).thenReturn(Optional.of(feedEntity.getPartnerTaxonomy()));
        when(taxonomyBlacklistRepository.findBySlug(feedEntity.getTaxonomyBlacklist().getSlug())).thenReturn(Optional.of(feedEntity.getTaxonomyBlacklist()));
        when(termsBlackListRepository.findBySlug(anyString())).thenReturn(Optional.of(feedEntity.getTermsBlacklist().get(0)));
        doNothing().when(productCollectionService).validateCollectionExists(anyLong());

        feedService.createFeed(feedEntity);

        verify(feedHistoryRepository).save(any(FeedHistory.class));
        verify(productCollectionService, never()).validateCollectionExists(anyLong());
    }

    @Test(expected = InvalidFeedException.class)
    public void testFeedValidationInvalidPartner(){
        PartnerEntity partnerEntity = Fixture.from(PartnerEntity.class).gimme("inactive-partner");
        when(feedRepository.findBySlug("someFeedSlug")).thenReturn(Optional.of(FeedEntity.builder()
                .collectionId(0L).
                        slug("someFeedSlug")
                .partner(PartnerEntity.builder().slug("anyPartnerSlug").build()).build()));
        when(partnerService.findBySlug("inactivePartner")).thenReturn(partnerEntity);
        doNothing().when(sendMailService).sendMail("someFeedSlug", " someFeedSlug", "Partner is not active/n");
        feedService.validateFeed("inactivePartner", "someFeedSlug");
    }

    @Test(expected = InvalidFeedException.class)
    public void testFeedValidationInvalidCollection(){
        PartnerEntity partnerEntity = Fixture.from(PartnerEntity.class).gimme(PartnerTemplateLoader.PARTNER_ENTITY);
        when(feedRepository.findBySlug("someFeedSlug")).thenReturn(Optional.of(FeedEntity.builder()
                .collectionId(0L).
                        slug("someFeedSlug")
                .partner(PartnerEntity.builder().slug("anyPartnerSlug").build()).build()));
        when(partnerService.findBySlug("anyPartnerSlug")).thenReturn(partnerEntity);
        doNothing().when(sendMailService).sendMail("someFeedSlug", " someFeedSlug", "Error/n");
        doAnswer(answer -> {throw new UserException("Error");}).when(productCollectionService).validateCollectionExists(0L);
        feedService.validateFeed("anyPartnerSlug", "someFeedSlug");
    }
}
