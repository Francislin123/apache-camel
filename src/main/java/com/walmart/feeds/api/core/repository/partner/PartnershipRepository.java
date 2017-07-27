package com.walmart.feeds.api.core.repository.partner;

import com.walmart.feeds.api.core.repository.partner.model.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, String> {

    Partnership findByReference(String reference);

}
