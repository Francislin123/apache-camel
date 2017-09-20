package com.walmart.feeds.api.core.service.blacklist.taxonomy;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistory;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistHistoryRepository;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TermsBlacklistServiceImpl implements TermsBlacklistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TermsBlacklistServiceImpl.class);

    @Autowired
    private TermsBlackListRepository termsBlacklistRepository;

    @Autowired
    private TermsBlacklistHistoryRepository termsBlacklistHistoryRepository;

    @Override
    @Transactional
    public void saveTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity) {

        if (termsBlacklistEntity == null) {
            throw new InconsistentEntityException("Null terms black list");
        }

        LOGGER.info("fieldsMappingHistory={} message=conflict", termsBlacklistEntity.getName());
        hasConflict(termsBlacklistEntity.getSlug());

        LOGGER.info("fieldsMappingHistory={} message=saved_successfully", termsBlacklistEntity.getName());
        persistTermsBlacklist(termsBlacklistEntity);
    }

    @Override
    public void hasConflict(String slug) {

        if (termsBlacklistRepository.findBySlug(slug).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Terms black list called '%s' already exists", slug));
        }
    }

    @Override
    @Transactional
    public void updateTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity) {

        String newSlug = SlugParserUtil.toSlug(termsBlacklistEntity.getName());

        if (!termsBlacklistEntity.getSlug().equals(newSlug)) {
            hasConflict(newSlug);
        }

        TermsBlacklistEntity currentPartner = findTermsBlacklistByReference(termsBlacklistEntity.getSlug());

        TermsBlacklistEntity updatedTermsBlacklist = TermsBlacklistEntity.builder()
                .id(currentPartner.getId())
                .creationDate(currentPartner.getCreationDate())
                .name(termsBlacklistEntity.getName())
                .slug(termsBlacklistEntity.getSlug())
                .list(termsBlacklistEntity.getList())
                .build();

        persistTermsBlacklist(updatedTermsBlacklist);
        LOGGER.info("termsBlacklistEntity={} message=update_successfully", termsBlacklistEntity.getName());
    }

    public TermsBlacklistEntity findBySlug(String slug) {

        LOGGER.info("termsBlacklistEntity={} message=findBy_successfully", slug);
        TermsBlacklistEntity termsBlacklistEntity = termsBlacklistRepository.findBySlug(slug).orElseThrow(() ->
                new EntityNotFoundException(String.format("Terms Black List %s not found!", slug)));
        return termsBlacklistEntity;

    }

    @Override
    public void deleteTermsBlacklist(String slug) {

        LOGGER.info("termsBlacklistEntity={} message=delete_successfully", slug);
        TermsBlacklistEntity termsBlackListDelete = findBySlug(slug);
        this.termsBlacklistRepository.delete(termsBlackListDelete);

    }

    private TermsBlacklistEntity persistTermsBlacklist(TermsBlacklistEntity termsBlacklist) {

        TermsBlacklistEntity saveTermsBlackList = termsBlacklistRepository.saveAndFlush(termsBlacklist);

        LOGGER.info("termsBlacklist={} message=saved_successfully", saveTermsBlackList);

        TermsBlacklistHistory termsBlacklistHistory = buildTermsBlacklistHistory(saveTermsBlackList);
        termsBlacklistHistory = termsBlacklistHistoryRepository.save(termsBlacklistHistory);

        LOGGER.info("termsBlacklistHistory={} message=saved_successfully", termsBlacklistHistory);

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

    private TermsBlacklistEntity findTermsBlacklistByReference(String slug) {
        return termsBlacklistRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(String.format("TermsBlacklist not found for slug='%s'", slug)));
    }
}
