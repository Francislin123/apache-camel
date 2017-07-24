package com.walmart.feeds.api.core.service.partner;

import java.util.List;

import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;

public interface PartnerService {

    /**
     *
     * @param pRequest
     * @return true if partner was saved
     */
    boolean savePartner(PartnerRequest pRequest);
    
    void updatePartner(PartnerRequest pRequest);
    
    PartnerRequest findByReference(String reference);

    List<Partner> getAllPartners();
}
