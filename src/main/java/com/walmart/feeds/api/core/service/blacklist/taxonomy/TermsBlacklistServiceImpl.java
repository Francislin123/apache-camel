package com.walmart.feeds.api.core.service.blacklist.taxonomy;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityInUseException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistory;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistoryRepository;
import com.walmart.feeds.api.core.repository.feed.FeedRepository;
import com.walmart.feeds.api.core.repository.feed.model.FeedEntity;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.common.response.SimpleError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TermsBlacklistServiceImpl implements TermsBlacklistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TermsBlacklistServiceImpl.class);

    @Autowired
    private TermsBlackListRepository termsBlacklistRepository;

    @Autowired
    private TermsBlacklistHistoryRepository termsBlacklistHistoryRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Override
    @Transactional
    public void saveTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity) {

        hasConflict(termsBlacklistEntity.getSlug());

        persistTermsBlacklist(termsBlacklistEntity);
    }

    @Override
    @Transactional
    public void updateTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity) {

        String newSlug = SlugParserUtil.toSlug(termsBlacklistEntity.getName());

        if (!termsBlacklistEntity.getSlug().equals(newSlug)) {
            hasConflict(newSlug);
        }

        TermsBlacklistEntity currentPartner = findTermsBlacklistBySlug(termsBlacklistEntity.getSlug());

        TermsBlacklistEntity updatedTermsBlacklist = TermsBlacklistEntity.builder()
                .id(currentPartner.getId())
                .creationDate(currentPartner.getCreationDate())
                .name(termsBlacklistEntity.getName())
                .slug(newSlug)
                .list(termsBlacklistEntity.getList())
                .build();

        persistTermsBlacklist(updatedTermsBlacklist);
    }

    public TermsBlacklistEntity findBySlug(String slug) {
        LOGGER.info("termsBlacklistEntity={} message=findBy_successfully", slug);
        return termsBlacklistRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(String.format("Terms Black List %s not found!", slug)));
    }

    @Override
    public List<TermsBlacklistEntity> findAllTermsBlacklistEntity() {
        return termsBlacklistRepository.findAll();
    }

    @Override
    public void deleteTermsBlacklist(String slug) {

        TermsBlacklistEntity toDelete = findBySlug(slug);

        List<FeedEntity> feeds = feedRepository.findByTermsBlacklist(toDelete);
        if (!feeds.isEmpty()) {
            List<SimpleError> feedsSlugs = feeds.stream().map(f -> SimpleError.builder().message(f.getSlug()).build()).collect(Collectors.toList());
            throw new EntityInUseException(String.format("Terms blacklist '%s' is being used by one or more feeds", slug), feedsSlugs);
        }

        LOGGER.info("termsBlacklistEntity={} message=delete_successfully", toDelete);
        this.termsBlacklistRepository.delete(toDelete);
    }

    @Override
    public void hasConflict(String slug) {

        LOGGER.info("termsBlacklistHistory={} message=conflict", slug);
        if (termsBlacklistRepository.findBySlug(slug).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Terms black list called '%s' already exists", slug));
        }
    }

    private TermsBlacklistEntity persistTermsBlacklist(TermsBlacklistEntity termsBlacklist) {

        TermsBlacklistEntity saveTermsBlackList = termsBlacklistRepository.saveAndFlush(termsBlacklist);

        TermsBlacklistHistory termsBlacklistHistory = buildTermsBlacklistHistory(saveTermsBlackList);

        LOGGER.info("termsBlacklistHistory={} message=saved_successfully", termsBlacklistHistory);
        termsBlacklistHistoryRepository.save(termsBlacklistHistory);

        return saveTermsBlackList;
    }

    private TermsBlacklistHistory buildTermsBlacklistHistory(TermsBlacklistEntity termsBlacklist) {
        return TermsBlacklistHistory.builder()
                .name(termsBlacklist.getName())
                .slug(termsBlacklist.getSlug())
                .list(String.valueOf(termsBlacklist.getList()))
                .creationDate(termsBlacklist.getCreationDate())
                .updateDate(termsBlacklist.getUpdateDate())
                .user(termsBlacklist.getUser())
                .build();
    }

    private TermsBlacklistEntity findTermsBlacklistBySlug(String slug) {
        return termsBlacklistRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(String.format("TermsBlacklist not found for slug='%s'", slug)));
    }
}
