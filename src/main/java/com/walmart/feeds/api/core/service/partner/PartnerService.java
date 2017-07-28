package com.walmart.feeds.api.core.service.partner;

import java.util.List;

import com.walmart.feeds.api.core.service.partner.model.PartnerTO;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import javassist.NotFoundException;
import org.springframework.data.jpa.repository.Query;

public interface PartnerService {

    /**
     *
     * @param partnerTO payload
     */
    void savePartner(PartnerTO partnerTO) throws IllegalArgumentException;

    PartnerTO findByReference(String reference) throws NotFoundException;

    List<PartnerTO> findAllPartners();

    List<PartnerTO> findActivePartners();

    List<PartnerTO> searchPartners(String query);

    /**
     *
     * @param partnerTO payload
     * @throws IllegalArgumentException when the partnerTO is not provided
     * @throws NotFoundException when partner not exists to be updated
     */
    void updatePartner(PartnerTO partnerTO) throws IllegalArgumentException, NotFoundException;

    /**
     *
     * @param reference for the partner
     * @param status true whether active, false otherwise
     */
    void setPartnerStatus(String reference, boolean status);

}
