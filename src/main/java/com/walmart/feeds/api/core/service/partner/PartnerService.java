package com.walmart.feeds.api.core.service.partner;

import com.walmart.feeds.api.core.exceptions.NotFoundException;
import com.walmart.feeds.api.core.service.partner.model.PartnerTO;

import java.util.List;

public interface PartnerService {

    /**
     *
     * @param partnerTO payload
     */
    void savePartner(PartnerTO partnerTO) throws IllegalArgumentException;

    PartnerTO findByReference(String reference) throws NotFoundException, com.walmart.feeds.api.core.exceptions.NotFoundException;

    List<PartnerTO> findAllPartners();

    List<PartnerTO> findActivePartners();

    List<PartnerTO> searchPartners(String query);

    /**
     *
     * @param partnerTO payload
     * @throws IllegalArgumentException when the partnerTO is not provided
     * @throws NotFoundException when partner not exists to be updated
     */
    void updatePartner(PartnerTO partnerTO) throws IllegalArgumentException, NotFoundException, com.walmart.feeds.api.core.exceptions.NotFoundException;

    /**
     *
     * @param reference for the partner
     * @param status true whether active, false otherwise
     */
    void changePartnerStatus(String reference, boolean status);

}
