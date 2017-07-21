package com.walmart.feeds.api.core.service.partner;

import com.walmart.feeds.api.core.repository.partner.PartnerRepository;
import com.walmart.feeds.api.core.repository.partner.PartnershipRepository;
import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.core.repository.partner.model.Partnership;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Service
public class PartnerService {

    private Logger logger = LoggerFactory.getLogger(PartnerService.class);

    @Autowired
    private PartnerRepository repository;
    @Autowired
    private PartnershipRepository partnershipRepository;

    public void savePartner(PartnerRequest pRequest) {

        Partner partner = buildPartner(pRequest);
        partner.setCreationDate(Calendar.getInstance());
        partner.setActive(true);

        partner = repository.save(partner);
        logger.info("Partner {} saved.", partner.getName());

    }

    public List<Partner> getAllPartners() {

        List<Partner> partners = repository.findAll();
        logger.info("Total of fetched partners: {}", partners.size());
        return partners;

    }

    private Partner buildPartner(PartnerRequest pRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(() -> {
            List<Partnership> partnerships = fetchPartnerships(pRequest.getPartnership());
            map().setPartnership(partnerships);
        });

        return modelMapper.map(pRequest, Partner.class);
    }

    private List<Partnership> fetchPartnerships(List<String> partnerships) {
        List<Partnership> partnershipList = new ArrayList();

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

}
