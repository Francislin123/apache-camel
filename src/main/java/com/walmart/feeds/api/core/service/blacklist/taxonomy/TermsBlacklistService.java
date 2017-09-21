package com.walmart.feeds.api.core.service.blacklist.taxonomy;

import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;

import java.util.List;

public interface TermsBlacklistService {

    /**
     * @param termsBlacklistEntity payload
     */

    void saveTermsBlacklist(TermsBlacklistEntity  termsBlacklistEntity);

    void hasConflict(String slug);

    void updateTermsBlacklist(TermsBlacklistEntity termsBlacklistEntity);

    void deleteTermsBlacklist(String slug);

    TermsBlacklistEntity findBySlug(String slug);

    List<TermsBlacklistEntity> findAllTermsBlacklistEntity();
}
