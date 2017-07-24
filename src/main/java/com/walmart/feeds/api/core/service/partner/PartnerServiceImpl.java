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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService {

    private Logger logger = LoggerFactory.getLogger(PartnerServiceImpl.class);

    @Autowired
    private PartnerRepository repository;
    @Autowired
    private PartnershipRepository partnershipRepository;

    @Override
    public boolean savePartner(PartnerRequest pRequest) {

        Partner partner = buildPartner(pRequest);
//        partner.setCreationDate(Calendar.getInstance());
        partner.setActive(true);

        partner = repository.save(partner);
        logger.info("Partner {} saved.", partner.getName());

        return partner != null;
    }

    @Override
    public List<Partner> getAllPartners() {

        List<Partner> partners = repository.findAll();
        logger.info("Total of fetched partners: {}", partners.size());
        return partners;

    }

    /**
     *
     * @param partnerRequest
     * @return new {@link Partner} based on {@link PartnerRequest}
     * @throws IllegalArgumentException if {@link PartnerRequest} is not provided
     */
    private Partner buildPartner(PartnerRequest partnerRequest) {
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
        List<Partnership> partnershipList = new ArrayList();

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

}
