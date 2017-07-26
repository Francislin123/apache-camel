package com.walmart.feeds.api.core.service.partner;

import java.util.List;

import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import com.walmart.feeds.api.resources.partner.response.PartnerResponse;

public interface PartnerService {

    /**
     *
     * @param partnerRequest
     * @return true if partner was saved
     */
    void savePartner(PartnerRequest partnerRequest);

    PartnerResponse findByReference(String reference);

    List<PartnerResponse> findAllPartners();

    List<PartnerResponse> findActivePartners();

    /**
     *
     * @param partnerRequest
     * @return false if
     */
    boolean updatePartner(PartnerRequest partnerRequest);

    /**
     *
     * @param reference
     * @param status
     * @return true if exists the partner
     */
    void setPartnerStatus(String reference, boolean status);
}
