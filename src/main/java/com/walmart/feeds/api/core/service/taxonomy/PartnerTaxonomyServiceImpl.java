package com.walmart.feeds.api.core.service.taxonomy;

import com.walmart.feeds.api.camel.TaxonomyMappingBindy;
import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyHistoryRepository;
import com.walmart.feeds.api.core.repository.taxonomy.PartnerTaxonomyRepository;
import com.walmart.feeds.api.core.repository.taxonomy.TaxonomyMappingRepository;
import com.walmart.feeds.api.core.repository.taxonomy.model.ImportStatus;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyEntity;
import com.walmart.feeds.api.core.repository.taxonomy.model.PartnerTaxonomyHistory;
import com.walmart.feeds.api.core.repository.taxonomy.model.TaxonomyMappingHistory;
import com.walmart.feeds.api.core.service.partner.PartnerService;
import com.walmart.feeds.api.resources.taxonomy.request.UploadTaxonomyMappingTO;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.walmart.feeds.api.camel.PartnerTaxonomyRouteBuilder.*;

@Service
public class PartnerTaxonomyServiceImpl implements PartnerTaxonomyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartnerTaxonomyServiceImpl.class);

    @Autowired
    private PartnerTaxonomyRepository partnerTaxonomyRepository;

    @Autowired
    private PartnerTaxonomyHistoryRepository partnerTaxonomyHistoryRepository;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private PersistPartnerTaxonomyCallBack persistPartnerTaxonomyCallBack;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private TaxonomyMappingRepository taxonomyMappingRepository;

    @Override
    @Transactional
    public void processFile(UploadTaxonomyMappingTO uploadTaxonomyMappingTO) throws IOException {

        PartnerEntity partner = partnerService.findBySlug(uploadTaxonomyMappingTO.getPartnerSlug());

        MultipartFile importedFile = uploadTaxonomyMappingTO.getTaxonomyMapping();

        String archiveName = FilenameUtils.getBaseName(importedFile.getOriginalFilename());

        PartnerTaxonomyEntity persistedTaxonomy = partnerTaxonomyRepository.findBySlug(uploadTaxonomyMappingTO.getSlug()).orElse(null);

        if (persistedTaxonomy != null &&
                (ImportStatus.INITIAL.equals(persistedTaxonomy.getStatus()) || ImportStatus.PENDING.equals(persistedTaxonomy.getStatus()))) {
            throw new EntityAlreadyExistsException("Partner Taxonomy still in processing status.");
        }

        List<TaxonomyMappingBindy> taxonomyMappingBindy = producerTemplate.requestBody(VALIDATE_FILE_ROUTE, importedFile.getInputStream(), List.class);

        PartnerTaxonomyEntity partnerTaxonomy = this.saveWithHistory(PartnerTaxonomyEntity.builder()
                .id(persistedTaxonomy == null ? null : persistedTaxonomy.getId())
                .slug(uploadTaxonomyMappingTO.getSlug())
                .name(uploadTaxonomyMappingTO.getName())
                .partner(partner)
                .fileName(archiveName)
                .status(ImportStatus.INITIAL)
                .taxonomyMappings(persistedTaxonomy == null ? null : persistedTaxonomy.getTaxonomyMappings())
                .creationDate(persistedTaxonomy == null ? null : persistedTaxonomy.getCreationDate())
                .build());

        Map<String, Object> map = new HashMap<>();
        map.put(PERSISTED_PARTNER_TAXONOMY, partnerTaxonomy);

        PartnerTaxonomyEntity entityToSave = producerTemplate.requestBodyAndHeaders(PARSE_BINDY_ROUTE, taxonomyMappingBindy, map, PartnerTaxonomyEntity.class);

        producerTemplate.asyncCallbackSendBody(PERSIST_PARTNER_TAXONOMY_ROUTE, entityToSave, persistPartnerTaxonomyCallBack);

    }

    @Override
    public void removeEntityBySlug(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        PartnerTaxonomyEntity partnerTaxonomyEntity = partnerTaxonomyRepository.findBySlugAndPartner(slug, partner).orElseThrow(() -> new EntityNotFoundException("Inexistent Partner Taxonomy"));
        partnerTaxonomyRepository.delete(partnerTaxonomyEntity);
    }

    @Override
    public List<PartnerTaxonomyEntity> fetchPartnerTaxonomies(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);
        List<PartnerTaxonomyEntity> list = new ArrayList<>();

        if(StringUtils.isEmpty(slug)) {
            list = partnerTaxonomyRepository.findByPartner(partner).orElseThrow(() -> new EntityNotFoundException(String.format("Partner Taxonomy not found for partner=%s", partnerSlug)));
        } else {
            list.add(partnerTaxonomyRepository.findBySlugAndPartner(slug, partner).orElseThrow(() -> new EntityNotFoundException(String.format("Partner Taxonomy not found for partner=%s and slug=%s", partnerSlug, slug))));
        }

        return list;
    }

    @Override
    public PartnerTaxonomyEntity fetchProcessedPartnerTaxonomy(String partnerSlug, String slug) {
        PartnerEntity partner = partnerService.findBySlug(partnerSlug);

        PartnerTaxonomyEntity entity = partnerTaxonomyRepository.findBySlugAndPartnerAndStatus(slug, partner, ImportStatus.PROCESSED).
                orElseThrow( () -> new EntityNotFoundException("Partner Taxonomy not found or imported with invalid status"));

        return entity;
    }

    @Override
    public PartnerTaxonomyEntity saveWithHistory(PartnerTaxonomyEntity entity) {

        PartnerTaxonomyEntity persistedEntity = partnerTaxonomyRepository.saveAndFlush(entity);

        PartnerTaxonomyHistory history = PartnerTaxonomyHistory.builder()
                .fileName(persistedEntity.getFileName())
                .name(persistedEntity.getName())
                .partnerTaxonomyId(persistedEntity.getId())
                .partner(persistedEntity.getPartner())
                .slug(persistedEntity.getSlug())
                .taxonomyMappings(
                        persistedEntity.getTaxonomyMappings() == null ? null : persistedEntity.getTaxonomyMappings().stream().map(association -> TaxonomyMappingHistory.builder()
                        .partnerPathId(association.getPartnerPathId())
                        .walmartPath(association.getWalmartPath())
                        .partnerPath(association.getPartnerPath())
                        .creationDate(association.getCreationDate())
                        .updateDate(association.getUpdateDate())
                        .user(association.getUser())
                        .build()).collect(Collectors.toList()))
                .status(persistedEntity.getStatus())
                .creationDate(persistedEntity.getCreationDate())
                .updateDate(persistedEntity.getUpdateDate())
                .user(persistedEntity.getUser())
                .build();

        partnerTaxonomyHistoryRepository.saveAndFlush(history);

        return persistedEntity;
    }

}
