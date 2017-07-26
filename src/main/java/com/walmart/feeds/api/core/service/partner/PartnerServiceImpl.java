package com.walmart.feeds.api.core.service.partner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.PartnershipRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.repository.partner.model.Partnership;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;

import javax.persistence.NoResultException;

@Service
public class PartnerServiceImpl implements PartnerService {

    private Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    private PartnerRepository repository;
    @Autowired
    private PartnershipRepository partnershipRepository;

    @Override
    public void savePartner(PartnerRequest partnerRequest) {
        Partner partner = buildPartner(partnerRequest);
        partner.setCreationDate(Calendar.getInstance());
        partner.setActive(true);
        
        partner = repository.save(partner);
        logger.info("Partner {} saved.", partner.getName());
    }

    /**
     *
     * @param partnerRequest
     * @return
     * @throws IllegalArgumentException
     * @see #buildPartner(PartnerRequest)
     */
    public boolean updatePartner(PartnerRequest partnerRequest) {

        if(partnerRequest == null) {
            logger.error("PartnerRequest not provided");
            return false;
        }

        Partner currentPartner = repository.findByReference(partnerRequest.getReference())
                .orElseThrow(NoResultException::new);

        Partner partner = buildPartner(partnerRequest);

        if(partner.getDescription() != null)
            currentPartner.setDescription(partner.getDescription());
        if(partner.getPartnership() != null)
            currentPartner.setPartnership(partner.getPartnership());

        currentPartner.setUpdateDate(Calendar.getInstance());

        repository.save(currentPartner);
        logger.info("Partner {} updated.", partner.getName());
        return true;
    }

    public PartnerResponse findByReference(String reference) {
        logger.info("PartnerRequest {} find.", reference.toString());

        Partner partner = repository.findByReference(reference)
                .orElseThrow(NoResultException::new);

        return buildPartnerResponse(partner);
    }

    @Override
    public List<PartnerResponse> findAllPartners() {

        List<Partner> partners = repository.findAll();
        logger.info("Total of fetched partners: {}", partners.size());
        return partners.stream().map(p -> buildPartnerResponse(p)).collect(Collectors.toList());

    }

    @Override
    public List<PartnerResponse> findActivePartners() {
        List<Partner> actives = repository.findPartnerActives();
        return actives.stream().map(p -> buildPartnerResponse(p)).collect(Collectors.toList());
    }


	public void setPartnerStatus(String reference, boolean newStatus) {
        logger.info("Changing partner {} status to {}", reference, newStatus);
        repository.changePartnerStatus(reference, newStatus);
    }

    /**
     *
     * @param partnerRequest
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
                List<Partnership> partnerships = fetchPartnerships(partnerRequest.getPartnership());
                map().setPartnership(partnerships);
            }
        });

        return modelMapper.map(partnerRequest, Partner.class);
    }

    private List<Partnership> fetchPartnerships(List<String> partnerships) {
        List<Partnership> partnershipList = new ArrayList<>();

        if (null == partnerships)
            return partnershipList;

        for (String type : partnerships) {
            try {
                Partnership partnership = partnershipRepository.findOne(type);
                if (null == partnership) {
                    continue;
                }
                partnershipList.add(partnership);
            } catch (IllegalArgumentException ex) {
                logger.error("Partnership " + type + " not found!", ex);
            }
        }
        return partnershipList;
        
    }

	private PartnerResponse buildPartnerResponse(Partner partner) throws IllegalArgumentException {
        if (partner == null)
            throw new IllegalArgumentException("Partner not provided.");

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Partner, PartnerRequest>() {
            @Override
            protected void configure() {
            		List<String> partnerships = 
            				partner.getPartnership().stream().map(Partnership::getName).collect(Collectors.toList());
                            
                map().setPartnership(partnerships);
            }
        });

        return modelMapper.map(partner, PartnerResponse.class);
    }
    
}
