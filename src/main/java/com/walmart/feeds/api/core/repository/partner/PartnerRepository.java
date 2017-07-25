package com.walmart.feeds.api.core.repository.partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.walmart.feeds.api.core.repository.partner.model.Partner;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

	Partner findByReference(String reference);

	@Modifying
	@Transactional
	@Query("UPDATE Partner p SET p.active = ?2 WHERE p.reference = ?1")
	void changePartnerStatus(String reference, boolean status);

}
