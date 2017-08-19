package com.walmart.feeds.api.core.service.commercialstructure;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureAssociationRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureHistoryRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.CommercialStructureRepository;
import com.walmart.feeds.api.core.repository.commercialstructure.model.*;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import com.walmart.feeds.api.resources.camel.CommercialStructureBindy;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.walmart.feeds.api.resources.camel.CommercialStructureRouteBuilder.PARSE_BINDY_ROUTE;
import static com.walmart.feeds.api.resources.camel.CommercialStructureRouteBuilder.VALIDATE_FILE_ROUTE;
import static com.walmart.feeds.api.resources.camel.CommercialStructureRouteBuilder.PERSIST_COMMERCIAL_STRUCTURE_ROUTE;

@Service
public class CommercialStructureServiceImpl implements CommercialStructureService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommercialStructureRepository commercialStructureRepository;

    @Autowired
    private CommercialStructureHistoryRepository commercialStructureHistoryRepository;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private PersistCommercialStructureCallBack persistCommercialStructureCallBack;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Override
    @Transactional
    public void processFile(String partnerSlug, MultipartFile importedFile) throws IOException {

        PartnerEntity partner = partnerService.findBySlug(partnerSlug);

        String archiveName = FilenameUtils.getBaseName(importedFile.getOriginalFilename());

        CommercialStructureEntity persistedCommercialStructure = commercialStructureRepository.findBySlug(SlugParserUtil.toSlug(archiveName)).orElse(null);

        if (persistedCommercialStructure != null && ImportStatus.PENDING.equals(persistedCommercialStructure.getStatus())) {
            throw new EntityAlreadyExistsException("Commercial Structure still in processing status.");
        }

        List<CommercialStructureBindy> parsedCommercialStructure = producerTemplate.requestBody(VALIDATE_FILE_ROUTE, importedFile.getInputStream(), List.class);

        if (persistedCommercialStructure != null) {
            deleteEntity(persistedCommercialStructure);
        }

        CommercialStructureEntity commercialStructure = this.saveWithHistory(CommercialStructureEntity.builder()
                .slug(SlugParserUtil.toSlug(archiveName))
                .partner(partner)
                .archiveName(archiveName)
                .status(ImportStatus.PENDING)
                .build());

        Map<String, Object> map = new HashMap<>();
        map.put("archiveName", archiveName);
        map.put("partner", partner);
        map.put("commercialStructure", commercialStructure);

        CommercialStructureEntity entityToSave = producerTemplate.requestBodyAndHeaders(PARSE_BINDY_ROUTE, parsedCommercialStructure, map, CommercialStructureEntity.class);

        producerTemplate.asyncCallbackSendBody(PERSIST_COMMERCIAL_STRUCTURE_ROUTE, entityToSave, persistCommercialStructureCallBack);

    }

    @Override
    public void removeEntityBySlug(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        CommercialStructureEntity commercialStructureEntity = commercialStructureRepository.findBySlug(slug).get();
        if(null == partner || null ==commercialStructureEntity){
            if(null == partner) {
                throw new EntityNotFoundException("Inexistent partner");
            }
            if(null == commercialStructureEntity) {
                throw new EntityNotFoundException("Inexistent Commercial Structure");
            }
        }else{
            commercialStructureRepository.delete(commercialStructureEntity);
        }
    }

    @Override
    public List<CommercialStructureEntity> fetchCommercialStructure(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        List<CommercialStructureEntity> list = new ArrayList<>();

        if(slug == null || StringUtils.isEmpty(slug)) {
            list = commercialStructureRepository.findByPartner(partner).orElseThrow(() -> new EntityNotFoundException(String.format("Commercial Structure not found for partner=%s", partnerSlug)));
        } else {
            list.add(commercialStructureRepository.findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(String.format("Commercial Structure not found for partner=%s and slug=%s", partnerSlug, slug))));
        }

        return list;
    }

    @Override
    public CommercialStructureEntity fetchOneCommercialStructure(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        if(null == partner){
            throw new EntityNotFoundException("Inexistent partner");
        }
        CommercialStructureEntity entity = commercialStructureRepository.findBySlug(slug).
                orElseThrow( () -> new EntityNotFoundException("Invalid commercial structure slug"));
        return entity;
    }

    @Override
    public CommercialStructureEntity saveWithHistory(CommercialStructureEntity entity) {

        CommercialStructureEntity persistedEntity = commercialStructureRepository.saveAndFlush(entity);

        CommercialStructureHistory history = CommercialStructureHistory.builder().archiveName(persistedEntity.getArchiveName())
                .commercialStructureId(persistedEntity.getId())
                .partner(persistedEntity.getPartner())
                .slug(persistedEntity.getSlug())
                .associationEntityList(
                        persistedEntity.getAssociationEntityList() == null ? null : persistedEntity.getAssociationEntityList().stream().map(association -> CommercialStructureAssociationHistory.builder()
                        .structurePartnerId(association.getStructurePartnerId())
                        .walmartTaxonomy(association.getWalmartTaxonomy())
                        .partnerTaxonomy(association.getPartnerTaxonomy())
                        .creationDate(association.getCreationDate())
                        .updateDate(association.getUpdateDate())
                        .user(association.getUser())
                        .build()).collect(Collectors.toList()))
                .status(persistedEntity.getStatus())
                .creationDate(persistedEntity.getCreationDate())
                .updateDate(persistedEntity.getUpdateDate())
                .user(persistedEntity.getUser())
                .build();

        commercialStructureHistoryRepository.saveAndFlush(history);

        return persistedEntity;
    }

    private void deleteEntity(CommercialStructureEntity entity){
        commercialStructureRepository.delete(entity);
        commercialStructureRepository.flush();
    }

}
