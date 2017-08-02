package com.walmart.feeds.api.core.service.partner;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.repository.partner.PartnerHistoryRepository;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import com.walmart.feeds.api.core.repository.partner.model.PartnerHistory;
import com.walmart.feeds.api.core.utils.SlugParserUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PartnerServiceImpl implements PartnerService {

    private Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private PartnerHistoryRepository partnerHistoryRepository;

    @Override
    public void savePartner(PartnerEntity partner) throws IllegalArgumentException {

        if (partner.getPartnerships().isEmpty()){
            logger.info("No partnership related with partner " + partner.getSlug());
            throw new IllegalArgumentException("No partnership related with partner " + partner.getSlug());
        }

        persistPartner(partner);

    }

    public void updatePartner(PartnerEntity partner) throws IllegalArgumentException, NotFoundException {

        if (partner == null) {
            logger.error("PartnerEntity not provided");
            throw new IllegalArgumentException("PartnerEntity not provided");
        }

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

    // TODO: 28/07/17 transactional
    private PartnerEntity persistPartner(PartnerEntity partner) {
        PartnerEntity savedPartner = partnerRepository.save(partner);

        logger.info("partner={} message=saved_successfully", savedPartner);

        // TODO: 28/07/17 The JPA not throw exception for inexistent entity updated.
        PartnerHistory partnerHistory = buildPartnerHistory(partner);
        partnerHistory = partnerHistoryRepository.save(partnerHistory);

        logger.info("partnerHistory={} message=saved_successfully", partnerHistory);

        return savedPartner;
    }

    public PartnerEntity findBySlug(String reference) throws NotFoundException {
        return findPartnerByReference(reference);
    }

    private PartnerEntity findPartnerByReference(String reference) throws NotFoundException {
        logger.info("Finding partner {}.", reference);

        return partnerRepository.findBySlug(reference)
                .orElseThrow(() -> new NotFoundException("PartnerEntity not found: " + reference));
    }

    @Override
    public List<PartnerEntity> findAllPartners() {

        List<PartnerEntity> partners = partnerRepository.findAll();
        logger.info("Total of fetched partners: {}", partners.size());
        return partners;

    }

    @Override
    public List<PartnerEntity> findActivePartners() {
        return partnerRepository.findPartnerActives();
    }

    @Override
    public List<PartnerEntity> searchPartners(String query) {
        return partnerRepository.searchPartners(query);
    }

    public void changePartnerStatus(String slug, boolean active) {
        logger.info("Changing partner {} status to {}", slug, active);
        partnerRepository.changePartnerStatus(slug, active);
    }

    private PartnerHistory buildPartnerHistory(PartnerEntity currentPartner) {
        ModelMapper modelMapper = new ModelMapper();
        PartnerHistory partnerHistory = modelMapper.map(currentPartner, PartnerHistory.class);
        return partnerHistory;
    }

}
