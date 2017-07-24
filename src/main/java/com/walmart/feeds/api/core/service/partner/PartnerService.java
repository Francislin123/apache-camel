package com.walmart.feeds.api.core.service.partner;

import java.util.List;

import com.walmart.feeds.api.core.repository.partner.model.Partner;
import com.walmart.feeds.api.resources.partner.request.PartnerRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PartnerService {

    /**
     *
     * @param pRequest
     * @return true if partner was saved
     */
    boolean savePartner(PartnerRequest pRequest);
    
    void updatePartner(PartnerRequest pRequest);

    void setPartnerStatus(String reference, boolean status);

    PartnerRequest findByReference(String reference);

    List<Partner> getAllPartners();
}
