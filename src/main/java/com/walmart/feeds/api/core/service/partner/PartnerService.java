package com.walmart.feeds.api.core.service.partner;

import com.walmart.feeds.api.core.exceptions.EntityAlreadyExistsException;
import com.walmart.feeds.api.core.exceptions.EntityNotFoundException;
import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;

import java.util.List;

public interface PartnerService {

    /**
     *
     * @param partner payload
     */
    void savePartner(PartnerEntity partner) throws IllegalArgumentException;

    PartnerEntity findBySlug(String reference);

    List<PartnerEntity> findPartnersByStatus(Boolean active);

    List<PartnerEntity> findAllPartners();

    List<PartnerEntity> searchPartners(String query);

    /**
     *
     * @param partner payload
     * @throws IllegalArgumentException when the partnerTO is not provided
     * @throws EntityNotFoundException when partner not exists to be updated
     */
    void updatePartner(PartnerEntity partner);

    /**
     *
     * @param reference for the partner
     * @param status true whether active, false otherwise
     */
    void changePartnerStatus(String reference, boolean status);

    public void hasConflict(String slug) throws EntityAlreadyExistsException ;

}
