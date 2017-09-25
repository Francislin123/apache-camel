package com.walmart.feeds.api.core.repository.blacklist;

import com.walmart.feeds.api.core.repository.blacklist.model.TermsBlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TermsBlackListRepository extends JpaRepository<TermsBlacklistEntity, UUID> {

    Optional<TermsBlacklistEntity> findBySlug(String slug);

}
