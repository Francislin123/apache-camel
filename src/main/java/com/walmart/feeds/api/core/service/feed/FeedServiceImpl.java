package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.client.tagadmin.TagAdmimCollectionClient;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.template.TemplateRepository;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by vn0y942 on 21/07/17.
 */
@Service
public class FeedServiceImpl implements FeedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedServiceImpl.class);

    @Autowired
    private TagAdmimCollectionClient tagAdminCollectionClient;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private FeedHistoryRepository feedHistoryRepository;

    @Autowired
    private TaxonomyBlacklistRepository taxonomyBlacklistRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private ProductCollectionService productCollectionService;

    @Override
    @Transactional
    public FeedEntity createFeed(FeedEntity feedEntity) {

        if (feedEntity.getPartner() == null) {
            throw new InconsistentEntityException("Feed must have a partner");
        }

        if (feedRepository.findBySlug(feedEntity.getSlug()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Feed with slug='%s' already exists", feedEntity.getSlug()));
        }

        PartnerEntity partner = partnerService.findActiveBySlug(feedEntity.getPartner().getSlug());

        TemplateEntity template = getTemplate(feedEntity) ;

        TaxonomyBlacklistEntity taxonomyBlacklist = getTaxonomyBlacklist(feedEntity);

        if (feedEntity.getCollectionId() != null) {
            productCollectionService.validateCollectionExists(feedEntity.getCollectionId());
        }

        FeedEntity newFeed = FeedEntity.builder()
                .utms(feedEntity.getUtms())
                .slug(feedEntity.getSlug())
                .type(feedEntity.getType())
                .partner(partner)
                .notificationUrl(feedEntity.getNotificationUrl())
                .notificationMethod(feedEntity.getNotificationMethod())
                .notificationFormat(feedEntity.getNotificationFormat())
                .name(feedEntity.getName())
                .active(feedEntity.isActive())
                .collectionId(feedEntity.getCollectionId())
                .creationDate(feedEntity.getCreationDate())
                .template(template)
                .taxonomyBlacklist(taxonomyBlacklist)
                .build();

        FeedEntity savedFeedEntity = saveFeedWithHistory(newFeed);

        LOGGER.info("feedEntity={} message=saved_successfully", savedFeedEntity);

        return savedFeedEntity;
    }

    @Override
    public FeedEntity fetchByPartner(String feedSlug, String partnerSlug) {

        partnerService.findBySlug(partnerSlug);

        return feedRepository.findBySlug(feedSlug).orElseThrow(() -> new EntityNotFoundException(String.format("FeedEntity not found for slug='%s'", feedSlug)));
    }

    @Override
    public List<FeedEntity> fetchActiveByPartner(String partnerSlug, Boolean active) {

        PartnerEntity partner = partnerService.findBySlug(partnerSlug);

        List<FeedEntity> feedsActivesByPartner;

        if (active == null) {
            feedsActivesByPartner = feedRepository.findByPartner(partner);
        } else {
            feedsActivesByPartner = feedRepository.findByActiveAndPartner(active, partner);
        }

        return feedsActivesByPartner;
    }

    @Override
    @Transactional
    public void changeFeedStatus(String partnerSlug, String slug, Boolean active) {

        partnerService.findBySlug(partnerSlug);

        FeedEntity feedEntity = feedRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(String.format("FeedEntity not found for slug='%s'", slug)));

        FeedEntity updatedFeed = FeedEntity.builder()
                .utms(feedEntity.getUtms())
                .id(feedEntity.getId())
                .slug(feedEntity.getSlug())
                .type(feedEntity.getType())
                .partner(feedEntity.getPartner())
                .notificationUrl(feedEntity.getNotificationUrl())
                .notificationMethod(feedEntity.getNotificationMethod())
                .notificationFormat(feedEntity.getNotificationFormat())
                .name(feedEntity.getName())
                .active(active)
                .template(feedEntity.getTemplate())
                .creationDate(feedEntity.getCreationDate())
                .build();

        saveFeedWithHistory(updatedFeed);

        LOGGER.info("feed={} message=updated_successfully", feedEntity);
    }

    @Override
    @Transactional
    public void updateFeed(FeedEntity feedEntity) {

        String newSlug = SlugParserUtil.toSlug(feedEntity.getName());

        if (!feedEntity.getSlug().equals(newSlug)) {
            hasConflict(newSlug);
        }

        FeedEntity persistedFeedEntity = feedRepository.findBySlug(feedEntity.getSlug()).orElseThrow(() -> new EntityNotFoundException("FeedEntity not Found"));

        PartnerEntity partner = partnerService.findBySlug(feedEntity.getPartner().getSlug());

        TemplateEntity template = getTemplate(feedEntity);

        TaxonomyBlacklistEntity taxonomyBlacklist = getTaxonomyBlacklist(feedEntity);

        if (feedEntity.getCollectionId() != null) {
            productCollectionService.validateCollectionExists(feedEntity.getCollectionId());
        }

        FeedEntity updatedFeed = FeedEntity.builder()
                .id(persistedFeedEntity.getId())
                .slug(newSlug)
                .name(feedEntity.getName())
                .type(feedEntity.getType())
                .partner(partner)
                .notificationUrl(feedEntity.getNotificationUrl())
                .notificationMethod(feedEntity.getNotificationMethod())
                .notificationFormat(feedEntity.getNotificationFormat())
                .utms(feedEntity.getUtms())
                .active(feedEntity.isActive())
                .collectionId(feedEntity.getCollectionId())
                .template(template)
                .taxonomyBlacklist(taxonomyBlacklist)
                .creationDate(persistedFeedEntity.getCreationDate())
                .build();
        saveFeedWithHistory(updatedFeed);

        LOGGER.info("feedEntity={} message=update_successfully", feedEntity);
    }

    @Override
    public void hasConflict(String slug) {

        if (feedRepository.findBySlug(slug).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("The feed called %s already exists", slug));
        }
    }

    private FeedEntity saveFeedWithHistory(FeedEntity feed) {
        FeedEntity savedFeed = feedRepository.saveAndFlush(feed);
        FeedHistory feedHistory = buildPartnerHistory(savedFeed);
        feedHistoryRepository.save(feedHistory);
        return savedFeed;
    }

    private TemplateEntity getTemplate(FeedEntity feedEntity) {
        if (feedEntity.getTemplate() != null) {
            return templateRepository.findBySlug(feedEntity.getTemplate().getSlug()).orElseThrow(() ->
                    new UserException(String.format("Template not found for reference %s", feedEntity.getTemplate().getSlug())));
        }

        return null;
    }

    private TaxonomyBlacklistEntity getTaxonomyBlacklist(FeedEntity feedEntity) {

        TaxonomyBlacklistEntity blacklist = feedEntity.getTaxonomyBlacklist();

        if (blacklist == null || blacklist.getSlug() == null || blacklist.getSlug().trim().isEmpty()) {
            return null;
        }

        return taxonomyBlacklistRepository.findBySlug(feedEntity.getTaxonomyBlacklist().getSlug()).orElseThrow(() ->
                new UserException(String.format("Taxonomy blacklist '%s' not found", feedEntity.getTaxonomyBlacklist().getSlug())));

    }

    private FeedHistory buildPartnerHistory(FeedEntity currentFeed) {

        FeedHistory feedHistory = FeedHistory.builder()
                .active(currentFeed.isActive())
                .creationDate(currentFeed.getCreationDate())
                .name(currentFeed.getName())
                .notificationFormat(currentFeed.getNotificationFormat().getType())
                .notificationMethod(currentFeed.getNotificationMethod().getType())
                .notificationUrl(currentFeed.getNotificationUrl())
                .partner(currentFeed.getPartner())
                .slug(currentFeed.getSlug())
                .type(currentFeed.getType())
                .template(currentFeed.getTemplate())
                .updateDate(currentFeed.getUpdateDate())
                .user(currentFeed.getUser())
                .build();

        return feedHistory;
    }
}
