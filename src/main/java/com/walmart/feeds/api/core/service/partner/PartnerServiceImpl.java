package com.walmart.feeds.api.core.service.partner;

import com.walmart.feeds.api.core.repository.partner.PartnerHistoryRepository;
import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.repository.partner.model.PartnerHistory;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PartnerServiceImpl implements PartnerService {

    private Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private PartnerHistoryRepository partnerHistoryRepository;

    @Override
    public void savePartner(PartnerTO partnerRequest) throws IllegalArgumentException {
        Partner partner = buildPartner(partnerRequest);

        if (partner.getPartnerships().isEmpty()){
            logger.info("No one partnership relationated with partner " + partner.getReference());
            throw new IllegalArgumentException("No one partnership relationated with partner " + partner.getReference());
        }

        partner.setCreationDate(LocalDateTime.now());
        partner.setActive(true);
        persistPartner(partner);

        logger.info("Partner {} saved.", partner.getName());
    }

    public void updatePartner(PartnerTO partnerRequest) throws IllegalArgumentException, NotFoundException {

        if(partnerRequest == null) {
            logger.error("PartnerTO not provided");
            throw new IllegalArgumentException("PartnerTO not provided");
        }

        Partner currentPartner = findPartnerByReference(partnerRequest.getReference());

        Partner partner = buildPartner(partnerRequest);

        if(partner.getDescription() != null)
            currentPartner.setDescription(partner.getDescription());
        if(partner.getPartnerships() != null)
            currentPartner.setPartnerships(partner.getPartnerships());

        currentPartner.setUpdateDate(LocalDateTime.now());
        persistPartner(currentPartner);

        logger.info("Partner {} updated.", currentPartner.getName());
    }

    // TODO: 28/07/17 transactional
    private void persistPartner(Partner partner) {
        partnerRepository.save(partner);
        // TODO: 28/07/17 The JPA not throw exception for inexistent entity updated.
        PartnerHistory partnerHistory = buildPartnerHistory(partner);
        partnerHistory = partnerHistoryRepository.save(partnerHistory);
    }

    public PartnerTO findByReference(String reference) throws NotFoundException {
        Partner partner = findPartnerByReference(reference);
        return buildPartnerTO(partner);
    }

    private Partner findPartnerByReference(String reference) throws NotFoundException {
        logger.info("Finding partner {}.", reference);

        return partnerRepository.findByReference(reference)
                .orElseThrow(() -> new NotFoundException("Partner not found: " + reference));
    }

    @Override
    public List<PartnerTO> findAllPartners() {

        List<Partner> partners = partnerRepository.findAll();
        logger.info("Total of fetched partners: {}", partners.size());
        return partners.stream().map(this::buildPartnerTO).collect(Collectors.toList());

    }

    @Override
    public List<PartnerTO> findActivePartners() {
        List<Partner> actives = partnerRepository.findPartnerActives();
        return actives.stream().map(this::buildPartnerTO).collect(Collectors.toList());
    }

    @Override
    public List<PartnerTO> searchPartners(String query) {
        List<Partner> partners = partnerRepository.searchPartners(query);
        return partners.stream().map(this::buildPartnerTO).collect(Collectors.toList());
    }

    public void setPartnerStatus(String reference, boolean newStatus) {
        logger.info("Changing partner {} status to {}", reference, newStatus);
        partnerRepository.changePartnerStatus(reference, newStatus);
    }

    /**
     *
     * @param partnerTO payload
     * @return new {@link Partner} based on {@link PartnerTO}
     * @throws IllegalArgumentException if {@link PartnerTO} is not provided
     */
    private Partner buildPartner(PartnerTO partnerTO) throws IllegalArgumentException {
        if (partnerTO == null)
            throw new IllegalArgumentException("PartnerTO not provided.");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PartnerTO, Partner>() {
            @Override
            protected void configure() {
                String[] partnerships = partnerTO.getPartnership()
                        .toArray(new String[partnerTO.getPartnership().size()]);
                map().setPartnerships(String.join(";", partnerships));
            }
        });

        return modelMapper.map(partnerTO, Partner.class);
    }

	private PartnerTO buildPartnerTO(Partner partner) throws IllegalArgumentException {
        if (partner == null)
            throw new IllegalArgumentException("Partner not provided.");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Partner, PartnerTO>() {
            @Override
            protected void configure() {
                String partnershipsString = partner.getPartnerships();
                if (partnershipsString != null) {
                    List<String> partnerships = Arrays.asList(partnershipsString.split(";"));
                    map().setPartnership(partnerships);
                }
            }
        });

        return modelMapper.map(partner, PartnerTO.class);
    }

    private PartnerHistory buildPartnerHistory(Partner currentPartner) {
        ModelMapper modelMapper = new ModelMapper();
        PartnerHistory partnerHistory = modelMapper.map(currentPartner, PartnerHistory.class);
        return partnerHistory;
    }

}
