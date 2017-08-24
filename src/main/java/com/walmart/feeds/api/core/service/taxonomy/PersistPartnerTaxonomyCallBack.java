package com.walmart.feeds.api.core.service.taxonomy;

import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyHistoryRepository;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyHistory;
import org.apache.camel.Exchange;
import org.apache.camel.spi.Synchronization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class PersistPartnerTaxonomyCallBack implements Synchronization {

    @Autowired
    private PartnerTaxonomyRepository partnerTaxonomyRepository;

    @Autowired
    private PartnerTaxonomyHistoryRepository partnerTaxonomyHistoryRepository;

    @Override
    @Transactional
    public void onComplete(Exchange exchange) {
        PartnerTaxonomyEntity body = exchange.getIn().getBody(PartnerTaxonomyEntity.class);

        PartnerTaxonomyEntity foundEntity = partnerTaxonomyRepository.findOne(body.getId());

        PartnerTaxonomyEntity persistedEntity = partnerTaxonomyRepository.saveAndFlush(PartnerTaxonomyEntity.builder()
                .id(foundEntity.getId())
                .status(ImportStatus.PROCESSED)
                .fileName(foundEntity.getFileName())
                .name(foundEntity.getName())
                .partner(foundEntity.getPartner())
                .slug(foundEntity.getSlug())
                .creationDate(foundEntity.getCreationDate())
                .taxonomyMappings(foundEntity.getTaxonomyMappings())
                .build());

        partnerTaxonomyHistoryRepository.save(PartnerTaxonomyHistory.builder()
                .partnerTaxonomyId(persistedEntity.getId())
                .status(persistedEntity.getStatus())
                .user(persistedEntity.getUser())
                .name(persistedEntity.getName())
                .creationDate(persistedEntity.getCreationDate())
                .updateDate(persistedEntity.getUpdateDate())
                .fileName(persistedEntity.getFileName())
                .partner(persistedEntity.getPartner())
                .slug(persistedEntity.getSlug())
                .build());

    }

    @Override
    @Transactional
    public void onFailure(Exchange exchange) {
        PartnerTaxonomyEntity body = exchange.getIn().getBody(PartnerTaxonomyEntity.class);

        PartnerTaxonomyEntity foundEntity = partnerTaxonomyRepository.findOne(body.getId());

        PartnerTaxonomyEntity persistedEntity = partnerTaxonomyRepository.saveAndFlush(PartnerTaxonomyEntity.builder()
                .id(foundEntity.getId())
                .status(ImportStatus.ERROR)
                .name(foundEntity.getName())
                .fileName(foundEntity.getFileName())
                .partner(foundEntity.getPartner())
                .slug(foundEntity.getSlug())
                .creationDate(foundEntity.getCreationDate())
                .taxonomyMappings(foundEntity.getTaxonomyMappings())
                .build());

        partnerTaxonomyHistoryRepository.save(PartnerTaxonomyHistory.builder()
                .partnerTaxonomyId(persistedEntity.getId())
                .status(persistedEntity.getStatus())
                .name(persistedEntity.getName())
                .user(persistedEntity.getUser())
                .creationDate(persistedEntity.getCreationDate())
                .updateDate(persistedEntity.getUpdateDate())
                .fileName(persistedEntity.getFileName())
                .partner(persistedEntity.getPartner())
                .slug(persistedEntity.getSlug())
                .build());
    }

}
