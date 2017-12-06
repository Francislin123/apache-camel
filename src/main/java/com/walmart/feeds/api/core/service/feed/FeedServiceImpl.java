package com.walmart.feeds.api.core.service.feed;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InvalidFeedException;
import com.walmart.feeds.api.core.exceptions.UserException;
import com.walmart.feeds.api.core.notifications.SendMailService;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistRepository;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyOwner;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.feed.FeedHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.repository.feed.model.FeedNotificationMethod;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.template.TemplateRepository;
import com.walmart.feeds.api.core.repository.template.model.TemplateEntity;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.TaxonomyBlacklistService;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions.TaxonomyBlacklistNotFoundException;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.exceptions.TermsBlacklistNotFoundException;
import com.walmart.feeds.api.core.service.blacklist.taxonomy.validation.TaxonomyBlacklistPartnerValidator;
import com.walmart.feeds.api.core.service.feed.model.FeedHistory;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.service.scheduler.FeedScheduler;
import com.walmart.feeds.api.core.service.scheduler.FeedSchedulerImpl;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedServiceImpl.class);

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private FeedHistoryRepository feedHistoryRepository;

    @Autowired
    private TaxonomyBlacklistRepository taxonomyBlacklistRepository;

    @Autowired
    private TermsBlackListRepository termsBlackListRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private ProductCollectionService productCollectionService;

    @Autowired
    private PartnerTaxonomyRepository partnerTaxonomyRepository;

    @Autowired
    private TaxonomyBlacklistService taxonomyBlacklistService;

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private FeedScheduler feedScheduler;

    @Autowired
    private CategoryCollectionService categoryCollectionService;

    @Override
    @Transactional
    public FeedEntity createFeed(FeedEntity feedEntity) {

        if (feedRepository.findBySlug(feedEntity.getSlug()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Feed with slug='%s' already exists", feedEntity.getSlug()));
        }

        PartnerEntity partner = partnerService.findActiveBySlug(feedEntity.getPartner().getSlug());

        TemplateEntity template = getTemplate(feedEntity);

        PartnerTaxonomyEntity partnerTaxonomyEntity = getPartnerTaxonomy(feedEntity, partner);

        TaxonomyBlacklistEntity taxonomyBlacklist = getTaxonomyBlacklist(feedEntity);

        TaxonomyBlacklistPartnerValidator.validatePartnerTaxonomiesOnBlacklist(taxonomyBlacklist, partnerTaxonomyEntity);

        List<TermsBlacklistEntity> termsBlacklist = getTermsBlacklist(feedEntity);

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
                .termsBlacklist(termsBlacklist)
                .partnerTaxonomy(partnerTaxonomyEntity)
                .cronPattern(feedEntity.getCronPattern() == null ? FeedSchedulerImpl.DEFAULT_CRON_INTERVAL : feedEntity.getCronPattern())
                .build();

        FeedEntity savedFeedEntity = saveFeedWithHistory(newFeed);

        LOGGER.info("feedEntity={} message=saved_successfully", savedFeedEntity);
        if (FeedNotificationMethod.FILE.equals(feedEntity.getNotificationMethod())) {
            LOGGER.info("creating schedule for entity {}", savedFeedEntity);
            feedScheduler.createFeedScheduler(savedFeedEntity.getSlug(), savedFeedEntity.getPartner().getSlug(), savedFeedEntity.getCronPattern());
        }

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
                .partnerTaxonomy(feedEntity.getPartnerTaxonomy())
                .cronPattern(feedEntity.getCronPattern())
                .creationDate(feedEntity.getCreationDate())
                .build();

        saveFeedWithHistory(updatedFeed);

        if (FeedNotificationMethod.FILE.equals(updatedFeed.getNotificationMethod())) {
            changeScheduleByFeedStatus(active, updatedFeed.getSlug(), updatedFeed.getPartner().getSlug(), updatedFeed.getCronPattern());
        }

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

        if (FeedNotificationMethod.FILE.equals(persistedFeedEntity.getNotificationMethod())) {
            changeScheduleByFeedStatus(false, persistedFeedEntity.getSlug(), persistedFeedEntity.getPartner().getSlug(), persistedFeedEntity.getCronPattern());
        }

        PartnerEntity partner = partnerService.findBySlug(feedEntity.getPartner().getSlug());

        TemplateEntity template = getTemplate(feedEntity);

        TaxonomyBlacklistEntity taxonomyBlacklist = getTaxonomyBlacklist(feedEntity);

        PartnerTaxonomyEntity partnerTaxonomyEntity = getPartnerTaxonomy(feedEntity, partner);

        TaxonomyBlacklistPartnerValidator.validatePartnerTaxonomiesOnBlacklist(taxonomyBlacklist, partnerTaxonomyEntity);

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
                .partnerTaxonomy(partnerTaxonomyEntity)
                .taxonomyBlacklist(taxonomyBlacklist)
                .termsBlacklist(getTermsBlacklist(feedEntity))
                .cronPattern(feedEntity.getCronPattern() == null ? FeedSchedulerImpl.DEFAULT_CRON_INTERVAL : feedEntity.getCronPattern())
                .creationDate(persistedFeedEntity.getCreationDate())
                .build();
        saveFeedWithHistory(updatedFeed);

        if (FeedNotificationMethod.FILE.equals(updatedFeed.getNotificationMethod())) {
            changeScheduleByFeedStatus(updatedFeed.isActive(), updatedFeed.getSlug(), updatedFeed.getPartner().getSlug(), updatedFeed.getCronPattern());
        }

        LOGGER.info("feedEntity={} message=update_successfully", feedEntity);
    }

    @Override
    public void hasConflict(String slug) {

        if (feedRepository.findBySlug(slug).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("The feed called %s already exists", slug));
        }
    }

    @Override
    public void validateFeed(String partnerSlug, String feedSlug) {
        LOGGER.debug("Start validation on feed: {} for partner : {}", feedSlug, partnerSlug);
        StringBuilder sb = new StringBuilder();

        FeedEntity feedEntity = feedRepository.findBySlug(feedSlug).get();

        validateFeedActivePartner(partnerSlug, sb);

        if (feedEntity.getCollectionId() != null) {
            validateCollection(feedEntity.getCollectionId(), sb);
        }

        if (feedEntity.getTaxonomyBlacklist() != null) {
            validateBlackList(feedEntity.getTaxonomyBlacklist(), feedSlug, partnerSlug);
        }

        if (sb.length() > 0) {

            String message = sb.toString();
            sendMailService.sendMail(feedEntity.getSlug(), feedEntity.getPartner().getSlug(), message);
            LOGGER.error("[Error] Feed error notification feed-name: {}, message: {}, partner-slug: {}", feedEntity.getSlug(), message, partnerSlug);
            throw new InvalidFeedException(message);
        }
        LOGGER.debug("End validation on feed: {} for partner : {}", feedSlug, partnerSlug);
    }

    @Override
    public TermsBlacklistEntity findTermBlacklistBySlug(String slug) {
        return termsBlackListRepository.findBySlug(slug).orElseThrow(() ->
                new UserException(String.format("Terms BlackList is not found for slug='%s", slug)));
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
                    new UserException(String.format("Template not found for slug='%s", feedEntity.getTemplate().getSlug())));
        }

        return null;
    }

    private PartnerTaxonomyEntity getPartnerTaxonomy(FeedEntity feedEntity, PartnerEntity partner) {
        PartnerTaxonomyEntity partnerTaxonomy = feedEntity.getPartnerTaxonomy();

        if (partnerTaxonomy == null || partnerTaxonomy.getSlug() == null || partnerTaxonomy.getSlug().trim().isEmpty()) {
            return null;
        }

        return partnerTaxonomyRepository.findBySlugAndPartner(partnerTaxonomy.getSlug(), partner).orElseThrow(() ->
                new UserException(String.format("Taxonomy not found for slug='%s' or does not belong to the partner=%s", partnerTaxonomy.getSlug(), partner.getSlug())));
    }

    private TaxonomyBlacklistEntity getTaxonomyBlacklist(FeedEntity feedEntity) {

        TaxonomyBlacklistEntity blacklist = feedEntity.getTaxonomyBlacklist();

        if (blacklist == null || blacklist.getSlug() == null || blacklist.getSlug().trim().isEmpty()) {
            return null;
        }

        return taxonomyBlacklistRepository.findBySlug(feedEntity.getTaxonomyBlacklist().getSlug()).orElseThrow(() ->
                new TaxonomyBlacklistNotFoundException(String.format("Taxonomy blacklist '%s' not found", feedEntity.getTaxonomyBlacklist().getSlug())));

    }

    private List<TermsBlacklistEntity> getTermsBlacklist(FeedEntity feedEntity) {

        if (feedEntity.getTermsBlacklist() == null || feedEntity.getTermsBlacklist().isEmpty()) {
            return null;
        }

        List<TermsBlacklistEntity> response = new ArrayList<>();

        feedEntity.getTermsBlacklist().forEach(termsBlacklistEntity -> response.add(termsBlackListRepository.findBySlug(termsBlacklistEntity.getSlug()).get()));

        if (response.isEmpty()) {
            throw new TermsBlacklistNotFoundException(String.format("Feeds entity %s not contains terms blacklist", feedEntity.getSlug()));
        }

        return response;
    }

    private FeedHistory buildPartnerHistory(FeedEntity currentFeed) {

        return FeedHistory.builder()
                .active(currentFeed.isActive())
                .creationDate(currentFeed.getCreationDate())
                .name(currentFeed.getName())
                .notificationFormat(currentFeed.getNotificationFormat().getType())
                .notificationMethod(currentFeed.getNotificationMethod().getType())
                .notificationUrl(currentFeed.getNotificationUrl())
                .partner(currentFeed.getPartner())
                .partnerTaxonomy(currentFeed.getPartnerTaxonomy())
                .slug(currentFeed.getSlug())
                .type(currentFeed.getType())
                .cronPattern(currentFeed.getCronPattern())
                .template(currentFeed.getTemplate())
                .updateDate(currentFeed.getUpdateDate())
                .user(currentFeed.getUser())
                .build();

    }

    private void validateFeedActivePartner(String partnerSlug, StringBuilder sb) {

        LOGGER.debug("Validating active partner: {} ", partnerSlug);
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        if (!partner.isActive()) {
            LOGGER.debug("Partner {} is not active: {} ", partnerSlug);
            sb.append(" Partner is not active" + System.lineSeparator());
        }
    }

    private void validateCollection(Long collectionId, StringBuilder sb) {
        LOGGER.debug("Validating collection:  {}", collectionId);
        try {
            productCollectionService.validateCollectionExists(collectionId);
        } catch (UserException ex) {
            LOGGER.debug("Invalid Collection:  {}", collectionId);
            sb.append(ex.getMessage() + System.lineSeparator());
        }
    }

    @Async
    public void validateBlackList(TaxonomyBlacklistEntity taxonomyBlacklistEntity, String partnerSlug, String feedSlug) {
        LOGGER.debug("Validating blacklist:  {}", taxonomyBlacklistEntity.getSlug());
        try {
            taxonomyBlacklistService.validateBlacklist(taxonomyBlacklistEntity);
        } catch (UserException ex) {
            LOGGER.error("Starting call to check if taxonomy exists and don't have any products");
            taxonomyBlacklistEntity.getList().forEach(taxonomy -> {
                if (taxonomy.getOwner().equals(TaxonomyOwner.WALMART) && !categoryCollectionService.validateTaxonomy(taxonomy.getTaxonomy())) {
                    sendMailService.sendMail(feedSlug, partnerSlug, " Error on Taxonomy blackList " +
                            taxonomyBlacklistEntity.getName() + ". Taxonomy '" + taxonomy.getTaxonomy() + "' does not exist in the catalog ");
                }
            });
        }
    }

    private void changeScheduleByFeedStatus(boolean status, String slug, String partnerSlug, String cronPattern) {

        if (status) {

            feedScheduler.createFeedScheduler(slug, partnerSlug, cronPattern);

        } else {

            feedScheduler.deleteJob(slug, partnerSlug);

        }
    }
}
