package com.walmart.feeds.api.core.service.blacklist.taxonomy;


import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;

import java.util.List;

public interface TaxonomyBlacklistService {

    TaxonomyBlacklistEntity create(TaxonomyBlacklistEntity taxonomyBlacklistEntity);

    void update(TaxonomyBlacklistEntity taxonomyBlacklistEntity);

    TaxonomyBlacklistEntity find(String slug);

    List<TaxonomyBlacklistEntity> findAll();

    void hasConflict(String slug);

    void deleteBySlug(String slug);

    List<TaxonomyBlacklistEntity> findBlackList(String taxonomyPath);

    void validateBlacklist(TaxonomyBlacklistEntity taxonomyBlacklistEntity);
}
