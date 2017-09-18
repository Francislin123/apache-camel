package com.walmart.feeds.api.core.service.blacklist.taxonomy;

import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;

public interface TermsBlacklistService {

    /**
     * @param termsBlacklistEntity payload
     */

    void saveTermsBlacklist(TermsBlacklistEntity  termsBlacklistEntity);

    void hasConflict(String slug);
}
