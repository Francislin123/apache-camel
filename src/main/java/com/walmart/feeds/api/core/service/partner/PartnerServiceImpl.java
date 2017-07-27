package com.walmart.feeds.api.core.service.partner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;

@Service
public class PartnerServiceImpl implements PartnerService {

    private Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private PartnershipRepository partnershipRepository;

    @Override
    public void savePartner(PartnerRequest partnerRequest) throws IllegalArgumentException {
        Partner partner = buildPartner(partnerRequest);
        partner.setCreationDate(Calendar.getInstance());
        partner.setActive(true);

        if (partner.getPartnerships().isEmpty()){
            logger.info("No one partnership relationated with partner " + partner.getReference());
            throw new IllegalArgumentException("No one partnership relationated with partner " + partner.getReference());
        }
        partner = partnerRepository.save(partner);
        logger.info("Partner {} saved.", partner.getName());
    }

    public void updatePartner(PartnerRequest partnerRequest) throws IllegalArgumentException, NotFoundException {

        if(partnerRequest == null) {
            logger.error("PartnerRequest not provided");
            throw new IllegalArgumentException("PartnerRequest not provided");
        }

        Partner currentPartner = findPartnerByReference(partnerRequest.getReference());

        Partner partner = buildPartner(partnerRequest);

        if(partner.getDescription() != null)
            currentPartner.setDescription(partner.getDescription());
        if(partner.getPartnerships() != null)
            currentPartner.setPartnerships(partner.getPartnerships());

        currentPartner.setUpdateDate(Calendar.getInstance());
        partnerRepository.save(currentPartner);
        logger.info("Partner {} updated.", partner.getName());

    }

    public PartnerResponse findByReference(String reference) throws NotFoundException {
        Partner partner = findPartnerByReference(reference);
        return buildPartnerResponse(partner);
    }

    private Partner findPartnerByReference(String reference) throws NotFoundException {
        logger.info("Finding partner {}.", reference);

        return partnerRepository.findByReference(reference)
                .orElseThrow(() -> new NotFoundException("Partner not found: " + reference));
    }

    @Override
    public List<PartnerResponse> findAllPartners() {

        List<Partner> partners = partnerRepository.findAll();
        logger.info("Total of fetched partners: {}", partners.size());
        return partners.stream().map(this::buildPartnerResponse).collect(Collectors.toList());

    }

    @Override
    public List<PartnerResponse> findActivePartners() {
        List<Partner> actives = partnerRepository.findPartnerActives();
        return actives.stream().map(this::buildPartnerResponse).collect(Collectors.toList());
    }

    @Override
    public List<PartnerResponse> searchPartners(String query) {
        List<Partner> partners = partnerRepository.searchPartners(query);
        return partners.stream().map(this::buildPartnerResponse).collect(Collectors.toList());
    }

    public void setPartnerStatus(String reference, boolean newStatus) {
        logger.info("Changing partner {} status to {}", reference, newStatus);
        partnerRepository.changePartnerStatus(reference, newStatus);
    }

    /**
     *
     * @param partnerRequest payload
     * @return new {@link Partner} based on {@link PartnerRequest}
     * @throws IllegalArgumentException if {@link PartnerRequest} is not provided
     */
    private Partner buildPartner(PartnerRequest partnerRequest) throws IllegalArgumentException {
        if (partnerRequest == null)
            throw new IllegalArgumentException("PartnerRequest not provided.");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PartnerRequest, Partner>() {
            @Override
            protected void configure() {
                String[] partnerships = partnerRequest.getPartnership()
                        .toArray(new String[partnerRequest.getPartnership().size()]);
                map().setPartnerships(String.join(";", partnerships));
            }
        });

        return modelMapper.map(partnerRequest, Partner.class);
    }

	private PartnerResponse buildPartnerResponse(Partner partner) throws IllegalArgumentException {
        if (partner == null)
            throw new IllegalArgumentException("Partner not provided.");

        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.addMappings(new PropertyMap<Partner, PartnerResponse>() {
//            @Override
//            protected void configure() {
//            		List<String> partnerships =
//            				partner.getPartnership().stream().map(Partnership::getName).collect(Collectors.toList());
//
//                map().setPartnership(partnerships);
//            }
//        });

        return modelMapper.map(partner, PartnerResponse.class);
    }
    
}
