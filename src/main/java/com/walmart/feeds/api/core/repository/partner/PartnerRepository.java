package com.walmart.feeds.api.core.repository.partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.walmart.feeds.api.core.repository.partner.model.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

	Partner findByReference(String reference);
	
}
