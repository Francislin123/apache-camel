package com.walmart.feeds.api.core.service.blacklist.taxonomy;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.repository.blacklist.TermsBlackListRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private void persistTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity) {

        termsBlacklistRepository.saveAndFlush(termsBlacklistEntity);
    }
}
