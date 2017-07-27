package com.walmart.feeds.api.core.service.partner;

import java.util.List;

import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;
import javassist.NotFoundException;
import org.springframework.data.jpa.repository.Query;

public interface PartnerService {

    /**
     *
     * @param partnerRequest payload
     */
    void savePartner(PartnerRequest partnerRequest) throws IllegalArgumentException;

    PartnerResponse findByReference(String reference) throws NotFoundException;

    List<PartnerResponse> findAllPartners();

    List<PartnerResponse> findActivePartners();

    List<PartnerResponse> searchPartners(String query);

    /**
     *
     * @param partnerRequest payload
     * @throws IllegalArgumentException when the partnerRequest is not provided
     * @throws NotFoundException when partner not exists to be updated
     */
    void updatePartner(PartnerRequest partnerRequest) throws IllegalArgumentException, NotFoundException;

    /**
     *
     * @param reference for the partner
     * @param status true whether active, false otherwise
     */
    void setPartnerStatus(String reference, boolean status);

}
