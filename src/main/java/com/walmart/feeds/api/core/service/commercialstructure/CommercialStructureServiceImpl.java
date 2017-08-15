package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureHistoryRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationHistory;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class CommercialStructureServiceImpl implements CommercialStructureService{

    @Autowired
    private CommercialStructureRepository commercialStructureRepository;

    @Autowired
    private CommercialStructureHistoryRepository commercialStructureHistoryRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public void loadFile(CommercialStructureEntity commercialStructureEntity){
        commercialStructureRepository.findBySlug(commercialStructureEntity.getSlug())
                .ifPresent(entity -> this.deleteEntity(entity));
        commercialStructureEntity = commercialStructureRepository.saveAndFlush(commercialStructureEntity);
        commercialStructureHistoryRepository.saveAndFlush(entityToHistoryTransform(commercialStructureEntity));

        logger.info("commercialStructureEntity={} message=update_successfully", commercialStructureEntity);
    }

    @Override
    public CommercialStructureHistory entityToHistoryTransform(CommercialStructureEntity commercialStructureEntity) {
        return CommercialStructureHistory.builder().archiveName(commercialStructureEntity.getArchiveName())
                .partner(commercialStructureEntity.getPartner())
                .slug(commercialStructureEntity.getSlug())
                .associationEntityList(commercialStructureEntity.getAssociationEntityList().stream().map(association -> CommercialStructureAssociationHistory.builder()
                .structurePartnerId(association.getStructurePartnerId())
                .walmartTaxonomy(association.getWalmartTaxonomy())
                .partnerTaxonomy(association.getPartnerTaxonomy()).build()).collect(Collectors.toList())).build();
    }
    private void deleteEntity(CommercialStructureEntity entity){
        commercialStructureRepository.delete(entity);
        commercialStructureRepository.flush();
    }

}
