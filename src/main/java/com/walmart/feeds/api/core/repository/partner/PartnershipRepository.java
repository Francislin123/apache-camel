package com.walmart.feeds.api.core.repository.partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.walmart.feeds.api.core.repository.partner.model.Partnership;

@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, String> {
}
