package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.template.TemplateRepository;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private FeedHistoryRepository feedHistoryRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    @Transactional
    public FeedEntity createFeed(FeedEntity feedEntity) {

        if (feedEntity.getPartner() == null) {
            throw new InconsistentEntityException("Feed must have a partner");
        }

        if (feedRepository.findBySlug(feedEntity.getSlug()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Feed with slug='%s' already exists", feedEntity.getSlug()));
        }

        PartnerEntity partner = partnerRepository.findActiveBySlug(feedEntity.getPartner().getSlug()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Partner slug='%s' not activated or not existent", feedEntity.getPartner().getSlug())));

        TemplateEntity template = templateRepository.findBySlug(feedEntity.getTemplate().getSlug()).orElseThrow(() ->
                new UserException(String.format("Template not found for reference %s", feedEntity.getTemplate().getSlug())));

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
                .creationDate(feedEntity.getCreationDate())
                .template(template)
                .build();

        FeedEntity savedFeedEntity = saveFeedWithHistory(newFeed);

        logger.info("feedEntity={} message=saved_successfully", savedFeedEntity);

        return savedFeedEntity;

    }

    @Override
    public FeedEntity fetchByPartner(String feedSlug, String partnerSlug) {

        partnerRepository.findBySlug(partnerSlug).orElseThrow(() -> new EntityNotFoundException(String.format("Partner slug='%s' not activated or not existent", partnerSlug)));

        return feedRepository.findBySlug(feedSlug).orElseThrow(() -> new EntityNotFoundException(String.format("FeedEntity not found for slug='%s'", feedSlug)));
    }

    @Override
    public List<FeedEntity> fetchActiveByPartner(String partnerSlug, Boolean active) {

        PartnerEntity partner = partnerRepository.findBySlug(partnerSlug).orElseThrow(() -> new EntityNotFoundException(String.format("Partner slug='%s' not activated or not existent", partnerSlug)));

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

        partnerRepository.findBySlug(partnerSlug).orElseThrow(() -> new EntityNotFoundException(String.format("Partner slug='%s' not activated or not existent", partnerSlug)));

        FeedEntity feedEntity = feedRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(String.format("FeedEntity not found for slug='%s'", slug)));//busca no banco a partir do slug

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

        logger.info("feed={} message=updated_successfully", feedEntity);
    }

    @Override
    @Transactional
    public void updateFeed(FeedEntity feedEntity) {

        if (feedEntity.getPartner() == null) {
            throw new InconsistentEntityException("Feed must have a partner");
        }

        PartnerEntity partner = partnerRepository.findBySlug(feedEntity.getPartner().getSlug()).orElseThrow(() -> new EntityNotFoundException("PartnerEntity not Found"));

        FeedEntity persistedFeedEntity = feedRepository.findBySlug(feedEntity.getSlug()).orElseThrow(() -> new EntityNotFoundException("FeedEntity not Found"));

        FeedEntity updatedFeed = FeedEntity.builder()
                .id(persistedFeedEntity.getId())
                .slug(SlugParserUtil.toSlug(feedEntity.getName()))
                .name(feedEntity.getName())
                .type(feedEntity.getType())
                .partner(partner)
                .notificationUrl(feedEntity.getNotificationUrl())
                .notificationMethod(feedEntity.getNotificationMethod())
                .notificationFormat(feedEntity.getNotificationFormat())
                .utms(feedEntity.getUtms())
                .active(feedEntity.isActive())
                .template(feedEntity.getTemplate())
                .creationDate(persistedFeedEntity.getCreationDate())
                .build();

        saveFeedWithHistory(updatedFeed);

        logger.info("feedEntity={} message=update_successfully", feedEntity);
    }

    private FeedEntity saveFeedWithHistory(FeedEntity feed) {
        FeedEntity savedFeed = feedRepository.saveAndFlush(feed);
        FeedHistory feedHistory = buildPartnerHistory(savedFeed);
        feedHistoryRepository.save(feedHistory);
        return savedFeed;
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
