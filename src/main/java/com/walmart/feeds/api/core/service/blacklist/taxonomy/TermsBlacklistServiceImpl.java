package com.walmart.feeds.api.core.service.blacklist.taxonomy;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;

@Service
public class TermsBlacklistServiceImpl implements TermsBlacklistService {

    @Autowired
    private TermsBlackListRepository termsBlacklistRepository;

    @Override
    @Transactional
    public void saveTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity) {

        if (termsBlacklistEntity == null) {
            throw new InconsistentEntityException("Null terms black list");
        }

        hasConflict(termsBlacklistEntity.getSlug());

        persistTermsBlacklist(termsBlacklistEntity);
    }

    @Override
    public void hasConflict(String slug) {

        if (termsBlacklistRepository.findBySlug(slug).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("Terms black list called '%s' already exists", slug));
        }
    }

    @Override
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
                .name(persistedEntity.getName())
                .slug(persistedEntity.getSlug())
                .list(persistedEntity.getList())
                .creationDate(persistedEntity.getCreationDate())
                .updateDate(persistedEntity.getUpdateDate())
                .user(persistedEntity.getUser())
                .build();

        persistTermsBlacklist(updatedEntity);


    }

    @Override
    public void deleteTermsBlacklist(String slug) {

        TermsBlacklistEntity fieldsMappingDelete = findBySlug(slug);
        this.termsBlacklistRepository.delete(fieldsMappingDelete);

    }

    public TermsBlacklistEntity findBySlug(String slug) {

        TermsBlacklistEntity termsBlacklistEntity = termsBlacklistRepository.findBySlug(slug).orElseThrow(() ->
                new EntityNotFoundException(String.format("Terms Black List %s not found!", slug)));
        return termsBlacklistEntity;

    }

    private void persistTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity) {

        termsBlacklistRepository.saveAndFlush(termsBlacklistEntity);
    }
}
