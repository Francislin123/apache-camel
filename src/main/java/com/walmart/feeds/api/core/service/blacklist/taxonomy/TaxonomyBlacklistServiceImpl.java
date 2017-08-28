package com.walmart.feeds.api.core.service.blacklist.taxonomy;


import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistHistoryRepository;
import com.walmart.feeds.api.core.repository.blacklist.TaxonomyBlacklistRepository;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistEntity;
import com.walmart.feeds.api.core.repository.blacklist.model.TaxonomyBlacklistHistory;
import com.walmart.feeds.api.core.utils.MapperUtil;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TaxonomyBlacklistServiceImpl implements TaxonomyBlacklistService{

    private static final Logger LOGGER = LoggerFactory.getLogger(TaxonomyBlacklistServiceImpl.class);

    @Autowired
    private TaxonomyBlacklistRepository taxonomyBlacklistRepository;

    @Autowired
    private TaxonomyBlacklistHistoryRepository taxonomyBlacklistHistoryRepository;

    @Override
    @Transactional
    public TaxonomyBlacklistEntity create(TaxonomyBlacklistEntity taxonomyBlacklistEntity) {

        TaxonomyBlacklistEntity entity = taxonomyBlacklistRepository.saveAndFlush(taxonomyBlacklistEntity);

        taxonomyBlacklistHistoryRepository.save(entityToHistory(entity));

        return entity;
    }

    @Override
    @Transactional
    public void update(TaxonomyBlacklistEntity taxonomyBlacklistEntity) {
        if(!SlugParserUtil.toSlug(taxonomyBlacklistEntity.getName()).equals(taxonomyBlacklistEntity.getSlug())){
            this.hasConflict(SlugParserUtil.toSlug(taxonomyBlacklistEntity.getName()));
        }
        TaxonomyBlacklistEntity persistedEntity = find(taxonomyBlacklistEntity.getSlug());


        TaxonomyBlacklistEntity toHistoryEntity = taxonomyBlacklistRepository.saveAndFlush(TaxonomyBlacklistEntity.builder()
                .creationDate(persistedEntity.getCreationDate())
                .id(persistedEntity.getId())
                .name(taxonomyBlacklistEntity.getName())
                .slug(SlugParserUtil.toSlug(taxonomyBlacklistEntity.getName()))
                .list(taxonomyBlacklistEntity.getList())
                .build()
        );
        taxonomyBlacklistHistoryRepository.save(entityToHistory(toHistoryEntity));

    }

    @Override
    public TaxonomyBlacklistEntity find(String slug) {
        return taxonomyBlacklistRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Taxonomy Blacklist '%s' not found", slug)));
    }

    @Override
    public List<TaxonomyBlacklistEntity> findAll() {
        return taxonomyBlacklistRepository.findAll();
    }

    private TaxonomyBlacklistHistory entityToHistory(TaxonomyBlacklistEntity entity){
        return TaxonomyBlacklistHistory.builder()
                .name(entity.getName())
                .slug(entity.getSlug())
                .list(MapperUtil.getMapsAsJson(entity.getList()))
                .creationDate(entity.getCreationDate())
                .updateDate(entity.getUpdateDate())
                .user(entity.getUser())
                .build();
    }
    @Override
    public void hasConflict(String slug) {
        if (taxonomyBlacklistRepository.findBySlug(slug).isPresent()) {
            LOGGER.info("taxonomyBlacklist={} error=already_exists", slug);
            throw new EntityAlreadyExistsException(String.format("The Taxonomy Blacklist called %s already exists", slug));
        }
    }
}
