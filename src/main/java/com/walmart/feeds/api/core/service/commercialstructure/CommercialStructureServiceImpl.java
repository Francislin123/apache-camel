package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureHistoryRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureAssociationHistory;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureEntity;
import com.walmart.feeds.api.core.repository.commercialstructure.model.CommercialStructureHistory;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommercialStructureServiceImpl implements CommercialStructureService{

    @Autowired
    private CommercialStructureRepository commercialStructureRepository;

    @Autowired
    private CommercialStructureHistoryRepository commercialStructureHistoryRepository;

    @Autowired
    private PartnerService partnerService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public void loadFile(CommercialStructureEntity commercialStructureEntity){
        PartnerEntity partner = partnerService.findBySlug(commercialStructureEntity.getPartner().getSlug());
        if(null == partner)
            throw  new EntityNotFoundException("Partner not found");
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

    @Override
    public void removeEntityBySlug(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        CommercialStructureEntity commercialStructureEntity = commercialStructureRepository.findBySlug(slug).get();
        if(null == partner || null ==commercialStructureEntity){
            if(null == partner)
                throw new EntityNotFoundException("Inexistent partner");
            if(null == commercialStructureEntity)
                throw new EntityNotFoundException("Inexistent Commercial Structure");
        }else{
            commercialStructureRepository.delete(commercialStructureEntity);
        }
    }

    @Override
    public List<CommercialStructureEntity> fetchCommercialStructure(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        List<CommercialStructureEntity> list = new ArrayList<>();
        if(null == partner){
            throw new EntityNotFoundException("Inexistent partner");
        }else{
            if(StringUtils.isEmpty(slug) || null == slug) {
                list = commercialStructureRepository.findByPartner(partner).get();
            }else{
                list.add(commercialStructureRepository.findBySlug(slug).get());
            }
        }
        return list;
    }

    @Override
    public CommercialStructureEntity fetchOneCommercialStructure(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        if(null == partner){
            throw new EntityNotFoundException("Inexistent partner");
        }

        return commercialStructureRepository.findBySlug(slug).get();
    }

    private void deleteEntity(CommercialStructureEntity entity){
        commercialStructureRepository.delete(entity);
        commercialStructureRepository.flush();
    }

}
