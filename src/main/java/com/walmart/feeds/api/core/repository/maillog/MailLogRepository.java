package com.walmart.feeds.api.core.repository.maillog;

import com.walmart.feeds.api.core.repository.maillog.model.MailLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailLogRepository extends JpaRepository<MailLogEntity, String> {
}
