package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureHistoryRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;
import com.walmart.feeds.api.core.repository.commercialstructure.model.ImportStatus;
import org.apache.camel.Exchange;
import org.apache.camel.spi.Synchronization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class PersistCommercialStructureCallBack implements Synchronization {

    @Autowired
    private CommercialStructureRepository commercialStructureRepository;

    @Autowired
    private CommercialStructureHistoryRepository commercialStructureHistoryRepository;

    @Override
    @Transactional
    public void onComplete(Exchange exchange) {
        CommercialStructureEntity body = exchange.getIn().getBody(CommercialStructureEntity.class);

        CommercialStructureEntity foundEntity = commercialStructureRepository.findOne(body.getId());

        CommercialStructureEntity persistedEntity = commercialStructureRepository.saveAndFlush(CommercialStructureEntity.builder()
                .id(foundEntity.getId())
                .status(ImportStatus.PROCESSED)
                .archiveName(foundEntity.getArchiveName())
                .partner(foundEntity.getPartner())
                .slug(foundEntity.getSlug())
                .creationDate(foundEntity.getCreationDate())
                .associationEntityList(foundEntity.getAssociationEntityList())
                .build());

        commercialStructureHistoryRepository.save(CommercialStructureHistory.builder()
                .commercialStructureId(persistedEntity.getId())
                .status(persistedEntity.getStatus())
                .user(persistedEntity.getUser())
                .creationDate(persistedEntity.getCreationDate())
                .updateDate(persistedEntity.getUpdateDate())
                .archiveName(persistedEntity.getArchiveName())
                .partner(persistedEntity.getPartner())
                .slug(persistedEntity.getSlug())
                .build());

    }

    @Override
    @Transactional
    public void onFailure(Exchange exchange) {
        CommercialStructureEntity body = exchange.getIn().getBody(CommercialStructureEntity.class);

        CommercialStructureEntity foundEntity = commercialStructureRepository.findOne(body.getId());

        CommercialStructureEntity persistedEntity = commercialStructureRepository.saveAndFlush(CommercialStructureEntity.builder()
                .id(foundEntity.getId())
                .status(ImportStatus.ERROR)
                .archiveName(foundEntity.getArchiveName())
                .partner(foundEntity.getPartner())
                .slug(foundEntity.getSlug())
                .creationDate(foundEntity.getCreationDate())
                .associationEntityList(foundEntity.getAssociationEntityList())
                .build());

        commercialStructureHistoryRepository.save(CommercialStructureHistory.builder()
                .commercialStructureId(persistedEntity.getId())
                .status(persistedEntity.getStatus())
                .user(persistedEntity.getUser())
                .creationDate(persistedEntity.getCreationDate())
                .updateDate(persistedEntity.getUpdateDate())
                .archiveName(persistedEntity.getArchiveName())
                .partner(persistedEntity.getPartner())
                .slug(persistedEntity.getSlug())
                .build());
    }

}
