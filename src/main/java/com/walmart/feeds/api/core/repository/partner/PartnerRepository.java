package com.walmart.feeds.api.core.repository.partner;

import com.walmart.feeds.api.core.repository.partner.model.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, UUID> {

    Optional<PartnerEntity> findBySlug(String slug);

    @Query("FROM PartnerEntity p where p.active=1 AND p.slug=?1")
    Optional<PartnerEntity> findActiveBySlug(String slug);

	@Modifying
	@Transactional()
    @Query("UPDATE PartnerEntity p SET p.active = ?2 WHERE p.slug = ?1")
    void changePartnerStatus(String reference, boolean status);

    @Query("SELECT p FROM PartnerEntity p WHERE active = true")
    List<PartnerEntity> findPartnerActives();

    @Query("SELECT DISTINCT p FROM PartnerEntity p WHERE " +
            "lower(p.name) LIKE %:query% OR " +
            "lower(p.slug) LIKE %:query% OR " +
            "lower(p.description) LIKE %:query% OR " +
			"lower(p.partnerships) LIKE %:query%"
	)
    List<PartnerEntity> searchPartners(@Param("query") String query);
}
