package com.walmart.feeds.api.core.service.partner;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.exceptions.InconsistentEntityException;
import com.walmart.feeds.api.core.repository.partner.PartnerHistoryRepository;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerHistory;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PartnerServiceImpl implements PartnerService {

    private Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private PartnerHistoryRepository partnerHistoryRepository;

    @Override
    @Transactional
    public void savePartner(PartnerEntity partner) {

        if (partner.getPartnerships().isEmpty()){
            logger.info("No partnership related with partner " + partner.getSlug());
            throw new InconsistentEntityException("No partnership related with partner " + partner.getSlug());
        }

        if (partnerRepository.findBySlug(partner.getSlug()).isPresent()) {
            logger.info("partner={} error=already_exists", partner);
            throw new EntityAlreadyExistsException(String.format("Partner with slug='%s' already exists", partner.getSlug()));
        }

        persistPartner(partner);

    }

    @Override
    @Transactional
    public void updatePartner(PartnerEntity partner) throws IllegalArgumentException, EntityNotFoundException {

        PartnerEntity currentPartner = findPartnerByReference(partner.getSlug());

        PartnerEntity updatedPartner = PartnerEntity.builder()
                .id(currentPartner.getId())
                .slug(SlugParserUtil.toSlug(partner.getName()))
                .name(partner.getName())
                .description(partner.getDescription())
                .partnerships(partner.getPartnerships())
                .creationDate(currentPartner.getCreationDate())
                .active(partner.isActive())
                .build();

        persistPartner(updatedPartner);

        logger.info("PartnerEntity {} updated.", partner.getSlug());
    }



    public PartnerEntity findBySlug(String reference) throws EntityNotFoundException {
        return findPartnerByReference(reference);
    }

    @Override
    public List<PartnerEntity> findPartnersByStatus(Boolean active) {

        List<PartnerEntity> partners;

        if (active == null) {
            partners = findAllPartners();
        } else {
            partners = partnerRepository.findByActive(active);
        }

        return partners;
    }

    @Override
    public List<PartnerEntity> findAllPartners() {

        List<PartnerEntity> partners = partnerRepository.findAll();
        logger.info("Total of fetched partners: {}", partners.size());
        return partners;

    }

    @Override
    public List<PartnerEntity> searchPartners(String query) {
        return partnerRepository.searchPartners(query);
    }

    @Override
    @Transactional
    public void changePartnerStatus(String slug, boolean active) throws EntityNotFoundException {
        logger.info("Changing partner {} status to {}", slug, active);

        PartnerEntity currentPartner = findPartnerByReference(slug);

        PartnerEntity updatedPartner = PartnerEntity.builder()
                .id(currentPartner.getId())
                .slug(currentPartner.getSlug())
                .name(currentPartner.getName())
                .description(currentPartner.getDescription())
                .partnerships(currentPartner.getPartnerships())
                .creationDate(currentPartner.getCreationDate())
                .active(active)
                .build();

        persistPartner(updatedPartner);

    }

    private PartnerHistory buildPartnerHistory(PartnerEntity currentPartner) {

        PartnerHistory partnerHistory = PartnerHistory.builder()
                .active(currentPartner.isActive())
                .creationDate(currentPartner.getCreationDate())
                .description(currentPartner.getDescription())
                .name(currentPartner.getName())
                .partnerships(currentPartner.getPartnerships())
                .slug(currentPartner.getSlug())
                .user(currentPartner.getUser())
                .updateDate(currentPartner.getUpdateDate())
                .build();

        return partnerHistory;
    }

    private PartnerEntity persistPartner(PartnerEntity partner) {

        PartnerEntity savedPartner = partnerRepository.saveAndFlush(partner);

        logger.info("partner={} message=saved_successfully", savedPartner);

        PartnerHistory partnerHistory = buildPartnerHistory(savedPartner);
        partnerHistory = partnerHistoryRepository.save(partnerHistory);

        logger.info("partnerHistory={} message=saved_successfully", partnerHistory);

        return savedPartner;
    }

    private PartnerEntity findPartnerByReference(String slug) throws EntityNotFoundException {
        logger.info("Finding partner {}.", slug);

        return partnerRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(String.format("PartnerEntity not for slug='%s'", slug)));
    }

}
