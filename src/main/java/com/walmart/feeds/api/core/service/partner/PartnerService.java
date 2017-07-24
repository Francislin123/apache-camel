package com.walmart.feeds.api.core.service.partner;

import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;

import java.util.List;

public interface PartnerService {

    /**
     *
     * @param pRequest
     * @return true if partner was saved
     */
    boolean savePartner(PartnerRequest pRequest);

    List<Partner> getAllPartners();
}
