package com.walmart.feeds.api.core.service.blacklist.taxonomy;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
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

        if (termsBlacklistEntity == null) {
            throw new InconsistentEntityException("Null terms black list");
        }

        String newSlug = SlugParserUtil.toSlug(termsBlacklistEntity.getName());

        if (!termsBlacklistEntity.getSlug().equals(newSlug)) {
            hasConflict(newSlug);
        }

        TermsBlacklistEntity persistedEntity = termsBlacklistRepository.findBySlug(termsBlacklistEntity.getSlug())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Terms Blacklist %s not found", termsBlacklistEntity.getSlug())));

        TermsBlacklistEntity updatedEntity = TermsBlacklistEntity.builder()
                .id(persistedEntity.getId())
                .name(termsBlacklistEntity.getName())
                .slug(termsBlacklistEntity.getSlug())
                .list(termsBlacklistEntity.getList())
                .creationDate(persistedEntity.getCreationDate())
                .build();

        LOGGER.info("termsBlacklistEntity={} message=update_successfully", termsBlacklistEntity.getName());
        persistTermsBlacklist(updatedEntity);
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

    private void persistTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity) {

        LOGGER.info("termsBlacklistEntity={} message=saved_successfully", termsBlacklistEntity.getName());
        termsBlacklistRepository.saveAndFlush(termsBlacklistEntity);
    }
}
