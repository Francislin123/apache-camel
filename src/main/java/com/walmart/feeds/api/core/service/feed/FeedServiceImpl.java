package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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

    @Override
    public FeedEntity createFeed(FeedEntity feedEntity) throws NotFoundException {

        if (feedEntity.getPartner() == null) {
            // TODO[r0i001q]: tratar excecao de maneira adequada
            throw new RuntimeException();
        }

        PartnerEntity partner = partnerRepository.findActiveBySlug(feedEntity.getPartner().getSlug()).orElseThrow(() ->
                new NotFoundException(String.format("PartnerEntity not found for reference %s", feedEntity.getPartner().getSlug())));

        FeedEntity newFeed = FeedEntity.builder()
                .utms(feedEntity.getUtms())
                .id(feedEntity.getId())
                .slug(feedEntity.getSlug())
                .type(feedEntity.getType())
                .partner(partner)
                .notificationUrl(feedEntity.getNotificationUrl())
                .notificationMethod(feedEntity.getNotificationMethod())
                .notificationFormat(feedEntity.getNotificationFormat())
                .name(feedEntity.getName())
                .active(feedEntity.isActive())
                .creationDate(feedEntity.getCreationDate())
                .build();

        FeedEntity savedFeedEntity = saveFeedWithHistory(newFeed);

        logger.info("feedEntity={} message=saved_successfully", savedFeedEntity);

        return savedFeedEntity;

    }

    @Override
    public List<FeedEntity> fetchByPartner(String partnerSlug, Boolean active) throws NotFoundException {

        PartnerEntity partner = partnerRepository.findBySlug(partnerSlug).orElseThrow(() -> new NotFoundException(String.format("PartnerEntity not found for slug %s", partnerSlug)));

        List<FeedEntity> feedsActivesByPartner;

        if (active == null) {
            feedsActivesByPartner = feedRepository.findByPartner(partner);
        } else {
            feedsActivesByPartner = feedRepository.findByActiveAndPartner(active, partner);
        }

        return feedsActivesByPartner;
    }

    @Override
    public void changeFeedStatus(String partnerSlug, String slug, Boolean active) throws NotFoundException {

        partnerRepository.findBySlug(partnerSlug).orElseThrow(() -> new NotFoundException("FeedEntity not Found"));

        FeedEntity feedEntityEntity = feedRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("FeedEntity not Found"));//busca no banco a partir do slug

        FeedEntity updatedFeed = FeedEntity.builder()
                .utms(feedEntityEntity.getUtms())
                .id(feedEntityEntity.getId())
                .slug(feedEntityEntity.getSlug())
                .type(feedEntityEntity.getType())
                .partner(feedEntityEntity.getPartner())
                .notificationUrl(feedEntityEntity.getNotificationUrl())
                .notificationMethod(feedEntityEntity.getNotificationMethod())
                .notificationFormat(feedEntityEntity.getNotificationFormat())
                .name(feedEntityEntity.getName())
                .active(active)
                .creationDate(feedEntityEntity.getCreationDate())
                .build();

        saveFeedWithHistory(updatedFeed);

        logger.info("feed={} message=updated_successfully", feedEntityEntity);
    }

    @Override
    public void updateFeed(FeedEntity feedEntity) throws DataIntegrityViolationException, NotFoundException {

        if (feedEntity.getPartner() == null) {
            // TODO[r0i001q]: tratar excecao de maneira adequada
            throw new RuntimeException();
        }

        PartnerEntity partner = partnerRepository.findBySlug(feedEntity.getPartner().getSlug()).orElseThrow(() -> new NotFoundException("PartnerEntity not Found"));

        FeedEntity persistedFeedEntity = feedRepository.findBySlug(feedEntity.getSlug()).orElseThrow(() -> new NotFoundException("FeedEntity not Found"));

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
                .creationDate(persistedFeedEntity.getCreationDate())
                .build();

        saveFeedWithHistory(updatedFeed);

        logger.info("feedEntity={} message=update_successfully", feedEntity);
    }

    private FeedEntity saveFeedWithHistory(FeedEntity feed) {
        FeedEntity savedFeed = feedRepository.save(feed);
        // TODO: 01/08/17 The JPA not throw exception for inexistent entity updated.
        FeedHistory feedHistory = buildPartnerHistory(savedFeed);
        feedHistoryRepository.save(feedHistory);
        return savedFeed;
    }

    private FeedHistory buildPartnerHistory(FeedEntity currentFeed) {

        FeedHistory feedHistory = FeedHistory.builder()
                .active(currentFeed.isActive())
                .creationDate(currentFeed.getCreationDate())
                .name(currentFeed.getName())
                .notificationFormat(currentFeed.getNotificationFormat())
                .notificationMethod(currentFeed.getNotificationMethod())
                .notificationUrl(currentFeed.getNotificationUrl())
                .partner(currentFeed.getPartner())
                .slug(currentFeed.getSlug())
                .type(currentFeed.getType())
                .updateDate(currentFeed.getUpdateDate())
                .user(currentFeed.getUser())
                .build();

        return feedHistory;
    }
}
