package com.walmart.feeds.api.core.repository.mailconf;

import com.walmart.feeds.api.core.repository.mailconf.model.MailConfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailConfRepository extends JpaRepository<MailConfEntity, String> {

    Optional<MailConfEntity> findBySlug(String slug);
}
