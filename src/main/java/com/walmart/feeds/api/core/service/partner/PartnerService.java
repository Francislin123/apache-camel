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
    void save(PartnerEntity partner) throws IllegalArgumentException;

    /**
     *
     * @param partner payload
     * @throws IllegalArgumentException when the partnerTO is not provided
     * @throws EntityNotFoundException when partner not exists to be updated
     */
    void update(PartnerEntity partner);

    /**
     *
     * @param reference for the partner
     * @param status true whether active, false otherwise
     */
    void changeStatus(String reference, boolean status);

    PartnerEntity findBySlug(String reference);

    List<PartnerEntity> findByStatus(Boolean active);

    List<PartnerEntity> findAll();

    List<PartnerEntity> search(String query);

    void hasConflict(String slug) throws EntityAlreadyExistsException ;

}
