package com.walmart.feeds.api.core.repository.partner;

import com.walmart.feeds.api.core.repository.partner.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

	Optional<Partner> findByReference(String reference);

	@Modifying
	@Transactional()
	@Query("UPDATE Partner p SET p.active = ?2 WHERE p.reference = ?1")
	void changePartnerStatus(String reference, boolean status);

    @Query("SELECT p FROM Partner p WHERE active = true")
    List<Partner> findPartnerActives();

    @Query("SELECT DISTINCT p FROM Partner p WHERE " +
			"lower(p.name) LIKE %:query% OR " +
			"lower(p.reference) LIKE %:query% OR " +
			"lower(p.description) LIKE %:query% OR " +
			"lower(p.partnerships) LIKE %:query%"
	)
	List<Partner> searchPartners(@Param("query") String query);
}
