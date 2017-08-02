package com.walmart.feeds.api.core.service.partner;

import java.util.List;

import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import javassist.NotFoundException;

public interface PartnerService {

    /**
     *
     * @param partner payload
     */
    void savePartner(PartnerEntity partner) throws IllegalArgumentException;

    PartnerEntity findBySlug(String reference) throws NotFoundException;

    List<PartnerEntity> findAllPartners();

    List<PartnerEntity> findActivePartners();

    List<PartnerEntity> searchPartners(String query);

    /**
     *
     * @param partner payload
     * @throws IllegalArgumentException when the partnerTO is not provided
     * @throws NotFoundException when partner not exists to be updated
     */
    void updatePartner(PartnerEntity partner) throws IllegalArgumentException, NotFoundException;

    /**
     *
     * @param reference for the partner
     * @param status true whether active, false otherwise
     */
    void setPartnerStatus(String reference, boolean status);

}
